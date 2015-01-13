/*
 * WebSphinx web-crawling toolkit
 *
 * Copyright (c) 1998-2002 Carnegie Mellon University.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY CARNEGIE MELLON UNIVERSITY ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL CARNEGIE MELLON UNIVERSITY
 * NOR ITS EMPLOYEES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package websphinx.workbench;

import java.awt.*;
import java.io.*;
import java.net.URL;
import websphinx.*;
import rcm.awt.Constrain;
import rcm.awt.PopupDialog;
import rcm.awt.TabPanel;
import rcm.awt.BorderPanel;
import rcm.awt.ClosableFrame;
import rcm.util.Win;

public class WorkbenchControlPanel extends PopupDialog {
    WebGraph g;
    WebOutline o;

    Choice nodeChoice;
    Choice pageChoice;
    Choice linkChoice;
    Checkbox automatic;

    Button applyButton;
    Button okButton;
    Button cancelButton;

    public WorkbenchControlPanel (WebGraph g, WebOutline o) {
        super (Win.findFrame (g != null ? (Component)g : (Component)o), "Workbench Control Panel", true);

        this.g = g;
        this.o = o;

        setLayout (new GridBagLayout ());

        Constrain.add (this, new Label ("Display:"),
                       Constrain.labelLike (0, 0));
        Constrain.add (this, nodeChoice = new Choice (),
                       Constrain.fieldLike (1, 0));
        nodeChoice.addItem ("icons");
        nodeChoice.addItem ("titles");
        nodeChoice.addItem ("absolute URLs");
        nodeChoice.addItem ("relative URLs");
        nodeChoice.select (g != null ? g.defaultRendering : o.defaultRendering+1);

        Constrain.add (this, new Label ("Pages:"),
                       Constrain.labelLike (0, 1));
        Constrain.add (this, pageChoice = new Choice (),
                       Constrain.fieldLike (1, 1));
        pageChoice.addItem ("visited pages");
        pageChoice.addItem ("all pages");

        Constrain.add (this, new Label ("Links:"),
                       Constrain.labelLike (0, 2));
        Constrain.add (this, linkChoice = new Choice (),
                       Constrain.fieldLike (1, 2));
        linkChoice.addItem ("tree links");
        linkChoice.addItem ("all links");

        if (g != null)
            switch (g.defaultFilter) {
                case WebGraph.NO_LINKS:
                case WebGraph.RETRIEVED_LINKS:
                    pageChoice.select (0);
                    linkChoice.select (0);
                    break;
                case WebGraph.WALKED_LINKS:
                case WebGraph.TREE_LINKS:
                    pageChoice.select (1);
                    linkChoice.select (0);
                    break;
                case WebGraph.ALL_LINKS:
                    pageChoice.select (1);
                    linkChoice.select (1);
                    break;
            }
        else {
            pageChoice.select (o.defaultFilter == WebOutline.ALL_LINKS ? 1 : 0);
            linkChoice.disable ();
        }


        Constrain.add (this, automatic = new Checkbox ("Automatic layout"),
                       Constrain.labelLike (1, 3));
        if (g != null)
            automatic.setState (g.getAutomaticLayout ());
        else
            g.disable ();

        Panel panel;
        Constrain.add (this, panel = new Panel(),
                       Constrain.centered (Constrain.labelLike (0, 4, 2)));
        panel.add (applyButton = new Button ("Apply"));
        panel.add (okButton = new Button ("OK"));
        panel.add (cancelButton = new Button ("Cancel"));

        pack ();
    }

    void writeBack () {
        if (g != null) g.setAutomaticLayout (automatic.getState ());

        switch (nodeChoice.getSelectedIndex ()) {
        case 0:
            if (g != null) g.setNodeRendering (WebGraph.ICON);
            if (o != null) o.setNodeRendering (WebOutline.TITLE);
            break;
        case 1:
            if (g != null) g.setNodeRendering (WebGraph.TITLE);
            if (o != null) o.setNodeRendering (WebOutline.TITLE);
            break;
        case 2:
            if (g != null) g.setNodeRendering (WebGraph.ABSOLUTE_URL);
            if (o != null) o.setNodeRendering (WebOutline.ABSOLUTE_URL);
            break;
        case 3:
            if (g != null) g.setNodeRendering (WebGraph.RELATIVE_URL);
            if (o != null) o.setNodeRendering (WebOutline.RELATIVE_URL);
            break;
        }

        switch (pageChoice.getSelectedIndex ()) {
        case 0:
            if (g != null) g.setLinkFilter (WebGraph.RETRIEVED_LINKS);
            if (o != null) o.setLinkFilter (WebOutline.RETRIEVED_LINKS);
            break;
        case 1:
            if (o != null) o.setLinkFilter (WebOutline.WALKED_LINKS);
            switch (linkChoice.getSelectedIndex ()) {
            case 0:
                if (g != null) g.setLinkFilter (WebGraph.WALKED_LINKS);
                break;
            case 1:
                if (g != null) g.setLinkFilter (WebGraph.ALL_LINKS);
                break;
            }
            break;
        }
    }

    public boolean handleEvent (Event event) {
        if (event.id == Event.ACTION_EVENT) {
            if (event.target == applyButton)
                writeBack ();
            else if (event.target == okButton) {
                writeBack ();
                close ();
            }
            else if (event.target == cancelButton)
                close ();
            else
                return super.handleEvent (event);
        }
        else if (event.id == Event.WINDOW_DESTROY)
            dispose ();
        else
            return super.handleEvent (event);

        return true;
    }
}
