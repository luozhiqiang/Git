/*      */ package symantec.itools.awt;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Event;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Panel;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Scrollbar;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class TreeView extends Panel
/*      */ {
/*      */   public static final int CHILD = 0;
/*      */   public static final int NEXT = 1;
/*      */   public static final int LAST = 2;
/*      */ 
/*      */   /** @deprecated */
/*      */   public static final int SEL_CHANGED = 1006;
/*      */   private TreeNode rootNode;
/*      */   private TreeNode selectedNode;
/*      */   private TreeNode topVisibleNode;
/*      */   protected Scrollbar verticalScrollBar;
/*      */   int sbVPosition;
/*      */   int sbVWidth;
/*      */   long sbVTimer;
/*      */   private boolean sbVShow;
/*      */   protected int count;
/*      */   protected int viewCount;
/*      */   protected Scrollbar horizontalScrollBar;
/*      */   int sbHPosition;
/*      */   int sbHHeight;
/*      */   private int sbHSize;
/*      */   private int newWidth;
/*      */   private boolean sbHShow;
/*      */   private int sbHLineIncrement;
/*      */   private int viewHeight;
/*      */   private int viewWidth;
/*      */   private int viewWidest;
/*      */   int cellSize;
/*      */   int clickSize;
/*      */   int imageInset;
/*      */   int textInset;
/*      */   int textBaseLine;
/*      */   private FontMetrics fm;
/*      */   protected boolean isSun1_1;
/*      */   protected Image im1;
/*      */   protected Graphics g1;
/*      */   private Vector e;
/*      */   private Vector v;
/*      */   protected boolean redrawTriggered;
/*      */   protected boolean treeChanged;
/*      */   protected boolean hasFocus;
/*      */ 
/*      */   public synchronized void clear()
/*      */   {
/*  197 */     this.rootNode = (this.selectedNode = null);
/*  198 */     this.count = 0;
/*  199 */     this.viewCount = 0;
/*  200 */     triggerRedraw();
/*      */   }
/*      */ 
/*      */   public void setTreeStructure(String[] s)
/*      */   {
/*  213 */     this.rootNode = (this.selectedNode = null);
/*      */     try
/*      */     {
/*  217 */       parseTreeStructure(s);
/*      */     }
/*      */     catch (InvalidTreeNodeException e)
/*      */     {
/*  221 */       System.out.println(e);
/*      */     }
/*      */ 
/*  224 */     triggerRedraw();
/*      */   }
/*      */ 
/*      */   public String[] getTreeStructure()
/*      */   {
/*  241 */     if (this.rootNode == null) return null;
/*  242 */     Vector nodesVector = new Vector(this.count);
/*  243 */     this.rootNode.depth = 0;
/*  244 */     vectorize(this.rootNode, false, nodesVector);
/*      */ 
/*  247 */     int numNodes = nodesVector.size();
/*  248 */     String[] treeStructure = new String[numNodes];
/*  249 */     for (int i = 0; i < numNodes; ++i)
/*      */     {
/*  251 */       TreeNode thisNode = (TreeNode)nodesVector.elementAt(i);
/*      */ 
/*  254 */       String treeString = "";
/*  255 */       for (int numBlanks = 0; numBlanks < thisNode.depth; ++numBlanks) {
/*  256 */         treeString = treeString + " ";
/*      */       }
/*      */ 
/*  259 */       treeString = treeString + thisNode.text;
/*      */ 
/*  262 */       treeStructure[i] = treeString;
/*      */     }
/*      */ 
/*  265 */     return treeStructure;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public void setFgHilite(Color c)
/*      */   {
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public Color getFgHilite()
/*      */   {
/*  287 */     return Color.black;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public void setBgHilite(Color c)
/*      */   {
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public Color getBgHilite()
/*      */   {
/*  305 */     return Color.white;
/*      */   }
/*      */ 
/*      */   public Object[] getSelectedObjects()
/*      */   {
/*  319 */     if (this.selectedNode == null) {
/*  320 */       return null;
/*      */     }
/*  322 */     TreeNode[] selectedObjects = new TreeNode[1];
/*  323 */     selectedObjects[0] = this.selectedNode;
/*      */ 
/*  325 */     return selectedObjects;
/*      */   }
/*      */ 
/*      */   public void insert(TreeNode newNode, TreeNode relativeNode, int position)
/*      */   {
/*  378 */     if ((newNode == null) || (relativeNode == null)) {
/*  379 */       return;
/*      */     }
/*  381 */     if (!exists(relativeNode)) {
/*  382 */       return;
/*      */     }
/*  384 */     switch (position)
/*      */     {
/*      */     case 0:
/*  387 */       addChild(newNode, relativeNode);
/*  388 */       break;
/*      */     case 1:
/*  391 */       addSibling(newNode, relativeNode, false);
/*  392 */       break;
/*      */     case 2:
/*  395 */       addSibling(newNode, relativeNode, true);
/*  396 */       break;
/*      */     default:
/*  400 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   public TreeNode getRootNode()
/*      */   {
/*  412 */     return this.rootNode;
/*      */   }
/*      */ 
/*      */   public int getCount()
/*      */   {
/*  420 */     return this.count;
/*      */   }
/*      */ 
/*      */   public int getViewCount()
/*      */   {
/*  429 */     return this.viewCount;
/*      */   }
/*      */ 
/*      */   boolean viewable(TreeNode node)
/*      */   {
/*  441 */     for (int i = 0; i < this.viewCount; ++i)
/*      */     {
/*  443 */       if (node == this.v.elementAt(i))
/*      */       {
/*  445 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*  449 */     return false;
/*      */   }
/*      */ 
/*      */   boolean viewable(String s)
/*      */   {
/*  461 */     if (s == null)
/*      */     {
/*  463 */       return false;
/*      */     }
/*      */ 
/*  466 */     for (int i = 0; i < this.viewCount; ++i)
/*      */     {
/*  468 */       TreeNode tn = (TreeNode)this.v.elementAt(i);
/*      */ 
/*  470 */       if ((tn.text != null) && 
/*  472 */         (s.equals(tn.text)))
/*      */       {
/*  474 */         return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  479 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean exists(TreeNode node)
/*      */   {
/*  490 */     recount();
/*      */ 
/*  492 */     for (int i = 0; i < this.count; ++i)
/*      */     {
/*  494 */       if (node == this.e.elementAt(i))
/*      */       {
/*  496 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*  500 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean exists(String s)
/*      */   {
/*  511 */     recount();
/*      */ 
/*  513 */     if (s == null)
/*      */     {
/*  515 */       return false;
/*      */     }
/*      */ 
/*  518 */     for (int i = 0; i < this.count; ++i)
/*      */     {
/*  520 */       TreeNode tn = (TreeNode)this.e.elementAt(i);
/*      */ 
/*  522 */       if ((tn.text != null) && 
/*  524 */         (s.equals(tn.text)))
/*      */       {
/*  526 */         return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  531 */     return false;
/*      */   }
/*      */ 
/*      */   public void append(TreeNode newNode)
/*      */   {
/*  544 */     if (this.rootNode == null)
/*      */     {
/*  546 */       this.rootNode = newNode;
/*  547 */       this.selectedNode = this.rootNode;
/*  548 */       this.count = 1;
/*  549 */       triggerRedraw();
/*      */     }
/*      */     else
/*      */     {
/*  553 */       addSibling(newNode, this.rootNode, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   void addChild(TreeNode newNode, TreeNode relativeNode)
/*      */   {
/*  559 */     if (relativeNode.child == null)
/*      */     {
/*  561 */       relativeNode.child = newNode;
/*  562 */       newNode.parent = relativeNode;
/*  563 */       this.count += 1;
/*  564 */       triggerRedraw();
/*      */     }
/*      */     else
/*      */     {
/*  568 */       addSibling(newNode, relativeNode.child, true);
/*      */     }
/*      */ 
/*  571 */     relativeNode.numberOfChildren += 1;
/*      */   }
/*      */ 
/*      */   void addSibling(TreeNode newNode, TreeNode siblingNode)
/*      */   {
/*  576 */     addSibling(newNode, siblingNode, true);
/*      */   }
/*      */ 
/*      */   void addSibling(TreeNode newNode, TreeNode siblingNode, boolean asLastSibling)
/*      */   {
/*  581 */     if (asLastSibling)
/*      */     {
/*  584 */       TreeNode tempNode = siblingNode;
/*  585 */       while (tempNode.sibling != null) {
/*  586 */         tempNode = tempNode.sibling;
/*      */       }
/*  588 */       tempNode.sibling = newNode;
/*      */     }
/*      */     else
/*      */     {
/*  593 */       newNode.sibling = siblingNode.sibling;
/*      */ 
/*  595 */       siblingNode.sibling = newNode;
/*      */     }
/*      */ 
/*  599 */     newNode.parent = siblingNode.parent;
/*      */ 
/*  601 */     this.count += 1;
/*  602 */     triggerRedraw();
/*      */   }
/*      */ 
/*      */   public TreeNode remove(String s)
/*      */   {
/*  614 */     recount();
/*      */ 
/*  616 */     for (int i = 0; i < this.count; ++i)
/*      */     {
/*  618 */       TreeNode tn = (TreeNode)this.e.elementAt(i);
/*      */ 
/*  620 */       if ((tn.text == null) || 
/*  622 */         (!s.equals(tn.text)))
/*      */         continue;
/*  624 */       remove(tn);
/*  625 */       triggerRedraw();
/*  626 */       return tn;
/*      */     }
/*      */ 
/*  631 */     return null;
/*      */   }
/*      */ 
/*      */   public void removeSelected()
/*      */   {
/*  641 */     if (this.selectedNode == null)
/*      */       return;
/*  643 */     remove(this.selectedNode);
/*      */   }
/*      */ 
/*      */   public void remove(TreeNode node)
/*      */   {
/*  656 */     if (!exists(node))
/*      */     {
/*  658 */       return;
/*      */     }
/*      */ 
/*  661 */     if (node == this.selectedNode)
/*      */     {
/*  663 */       int index = this.v.indexOf(this.selectedNode);
/*      */ 
/*  665 */       if (index == -1)
/*      */       {
/*  667 */         index = this.e.indexOf(this.selectedNode);
/*      */       }
/*      */ 
/*  670 */       if (index > this.viewCount - 1)
/*      */       {
/*  672 */         index = this.viewCount - 1;
/*      */       }
/*      */ 
/*  675 */       if (index > 0)
/*      */       {
/*  677 */         changeSelection((TreeNode)this.v.elementAt(index - 1));
/*      */       }
/*  679 */       else if (this.viewCount > 1)
/*      */       {
/*  681 */         changeSelection((TreeNode)this.v.elementAt(1));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  686 */     if (node.parent != null)
/*      */     {
/*  688 */       if (node.parent.child == node)
/*      */       {
/*  690 */         if (node.sibling != null)
/*      */         {
/*  692 */           node.parent.child = node.sibling;
/*      */         }
/*      */         else
/*      */         {
/*  696 */           node.parent.child = null;
/*  697 */           node.parent.collapse();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  702 */         TreeNode tn = node.parent.child;
/*      */ 
/*  704 */         while (tn.sibling != node)
/*      */         {
/*  706 */           tn = tn.sibling;
/*      */         }
/*      */ 
/*  709 */         if (node.sibling != null)
/*      */         {
/*  711 */           tn.sibling = node.sibling;
/*      */         }
/*      */         else
/*      */         {
/*  715 */           tn.sibling = null;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*  721 */     else if (node == this.rootNode)
/*      */     {
/*  723 */       if (node.sibling == null)
/*      */       {
/*  725 */         this.rootNode = null;
/*      */       }
/*      */       else
/*      */       {
/*  729 */         this.rootNode = node.sibling;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  734 */       TreeNode tn = this.rootNode;
/*      */ 
/*  736 */       while (tn.sibling != node)
/*      */       {
/*  738 */         tn = tn.sibling;
/*      */       }
/*      */ 
/*  741 */       if (node.sibling != null)
/*      */       {
/*  743 */         tn.sibling = node.sibling;
/*      */       }
/*      */       else
/*      */       {
/*  747 */         tn.sibling = null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  752 */     recount();
/*  753 */     triggerRedraw();
/*      */   }
/*      */ 
/*      */   public void printTree(TreeNode node)
/*      */   {
/*  765 */     if (node == null)
/*      */     {
/*  767 */       return;
/*      */     }
/*      */ 
/*  770 */     System.out.println(node.text);
/*  771 */     printTree(node.child);
/*  772 */     printTree(node.sibling);
/*      */   }
/*      */ 
/*      */   private void recount()
/*      */   {
/*  778 */     this.count = 0;
/*  779 */     this.e = new Vector();
/*      */ 
/*  781 */     if (this.rootNode == null)
/*      */       return;
/*  783 */     this.rootNode.depth = 0;
/*  784 */     traverse(this.rootNode);
/*      */   }
/*      */ 
/*      */   private void traverse(TreeNode node)
/*      */   {
/*  790 */     this.count += 1;
/*  791 */     this.e.addElement(node);
/*      */ 
/*  793 */     if (node.child != null)
/*      */     {
/*  795 */       node.child.depth = (node.depth + 1);
/*  796 */       traverse(node.child);
/*      */     }
/*  798 */     if (node.sibling == null)
/*      */       return;
/*  800 */     node.sibling.depth = node.depth;
/*  801 */     traverse(node.sibling);
/*      */   }
/*      */ 
/*      */   private void resetVector()
/*      */   {
/*  811 */     this.v = new Vector(this.count);
/*  812 */     this.viewWidest = 30;
/*      */ 
/*  814 */     if (this.count < 1)
/*      */     {
/*  816 */       this.viewCount = 0;
/*  817 */       return;
/*      */     }
/*      */ 
/*  820 */     this.rootNode.depth = 0;
/*  821 */     vectorize(this.rootNode, true, this.v);
/*  822 */     this.viewCount = this.v.size();
/*      */   }
/*      */ 
/*      */   private void vectorize(TreeNode node, boolean respectExpanded, Vector nodeVector)
/*      */   {
/*  830 */     if (node == null) {
/*  831 */       return;
/*      */     }
/*  833 */     nodeVector.addElement(node);
/*      */ 
/*  835 */     if (((!respectExpanded) && (node.child != null)) || (node.isExpanded()))
/*      */     {
/*  837 */       node.child.depth = (node.depth + 1);
/*  838 */       vectorize(node.child, respectExpanded, nodeVector);
/*      */     }
/*      */ 
/*  841 */     if (node.sibling == null)
/*      */       return;
/*  843 */     node.sibling.depth = node.depth;
/*  844 */     vectorize(node.sibling, respectExpanded, nodeVector);
/*      */   }
/*      */ 
/*      */   private void debugVector()
/*      */   {
/*  850 */     int vSize = this.v.size();
/*      */ 
/*  852 */     for (int i = 0; i < this.count; ++i)
/*      */     {
/*  854 */       TreeNode node = (TreeNode)this.v.elementAt(i);
/*  855 */       System.out.println(node.text);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized boolean handleEvent(Event event)
/*      */   {
/*  867 */     if (event.target == this.verticalScrollBar)
/*      */     {
/*  869 */       if (this.sbVPosition != this.verticalScrollBar.getValue())
/*      */       {
/*  871 */         this.sbVPosition = this.verticalScrollBar.getValue();
/*  872 */         scrolled();
/*      */       }
/*  874 */       return true;
/*      */     }
/*  876 */     if (event.target == this.horizontalScrollBar)
/*      */     {
/*  878 */       if (this.sbHPosition != this.horizontalScrollBar.getValue())
/*      */       {
/*  880 */         this.sbHPosition = this.horizontalScrollBar.getValue();
/*  881 */         super.repaint();
/*      */       }
/*  883 */       return true;
/*      */     }
/*      */ 
/*  886 */     return super.handleEvent(event);
/*      */   }
/*      */ 
/*      */   public boolean mouseDown(Event event, int x, int y)
/*      */   {
/*  910 */     super.requestFocus();
/*      */ 
/*  915 */     int index = y / this.cellSize + this.sbVPosition;
/*      */ 
/*  918 */     if (index > this.viewCount - 1) {
/*  919 */       return true;
/*      */     }
/*  921 */     TreeNode oldNode = this.selectedNode;
/*  922 */     TreeNode newNode = (TreeNode)this.v.elementAt(index);
/*  923 */     int newDepth = newNode.getDepth();
/*      */ 
/*  925 */     changeSelection(newNode);
/*      */ 
/*  928 */     Rectangle toggleBox = new Rectangle(this.cellSize * newDepth + this.cellSize / 4 - this.sbHPosition, (index - this.sbVPosition) * this.cellSize + this.clickSize / 2, this.clickSize, this.clickSize);
/*      */ 
/*  932 */     if (toggleBox.inside(x, y))
/*      */     {
/*  934 */       newNode.toggle();
/*      */ 
/*  936 */       triggerRedraw();
/*      */     }
/*  941 */     else if ((newNode == oldNode) && (event.clickCount == 2))
/*      */     {
/*  945 */       sendActionEvent();
/*      */     }
/*      */ 
/*  954 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean keyDown(Event event, int key)
/*      */   {
/*  975 */     int index = this.v.indexOf(this.selectedNode);
/*      */ 
/*  977 */     switch (key)
/*      */     {
/*      */     case 13:
/*  981 */       sendActionEvent();
/*  982 */       super.requestFocus();
/*  983 */       break;
/*      */     case 1006:
/*  985 */       if (event.controlDown())
/*      */       {
/*  987 */         if (this.sbHPosition > 0)
/*      */         {
/*  989 */           this.horizontalScrollBar.setValue(Math.max(this.sbHPosition -= this.sbHLineIncrement, 0));
/*  990 */           super.repaint();
/*      */         }
/*  992 */        // break label350;
break;
/*      */       }
/*      */ 
/*  995 */       if (!this.selectedNode.isExpanded())
/*      */        // break label145;
	break;
/*  997 */       this.selectedNode.toggle();
/*  998 */       triggerRedraw();
/*  999 */       break;
/*      */     case 1004:
/* 1004 */       if (index <= 0)
/*      */       //  break label171;
	break;
/* 1006 */       --index;
/* 1007 */       changeSelection((TreeNode)this.v.elementAt(index));
/* 1008 */       super.requestFocus();
/*      */ 
/* 1010 */       break;
/*      */     case 1007:
/* 1012 */       if (event.controlDown())
/*      */       {
/* 1014 */         int max = this.horizontalScrollBar.getMaximum() - ((this.isSun1_1) ? super.size().width - this.sbVWidth : 0);
/* 1015 */         if ((this.sbHShow) && (this.sbHPosition < max))
/*      */         {
/* 1017 */           this.horizontalScrollBar.setValue(Math.min(this.sbHPosition += this.sbHLineIncrement, max));
/* 1018 */           super.repaint();
/*      */         }
/* 1020 */        // break label350;
break;
/*      */       }
/*      */ 
/* 1023 */       if ((this.selectedNode.isExpandable()) && (!this.selectedNode.isExpanded()))
/*      */       {
/* 1025 */         this.selectedNode.toggle();
/*      */ 
/* 1027 */         triggerRedraw();
/* 1028 */      //   break label350;
break;
/*      */       }
/*      */ 
/* 1031 */       if (!this.selectedNode.isExpandable())
/*      */         //break label350;
	break;
/*      */     case 1005:
/* 1037 */       if (index >= this.viewCount - 1)
/*      */       //  break label345;
	break;
/* 1039 */       ++index;
/* 1040 */       changeSelection((TreeNode)this.v.elementAt(index));
/* 1041 */       super.requestFocus();
/* 1042 */      // break label350;
break;
/*      */ 
/* 1044 */     //  break;
/*      */     default:
/* 1047 */       label145: label171: label345: return false;
/*      */     }
/* 1049 */     label350: return true;
/*      */   }
/*      */ 
/*      */   public boolean gotFocus(Event e, Object what)
/*      */   {
/* 1057 */     this.hasFocus = true;
/* 1058 */     if ((this.selectedNode != null) && (this.v != null))
/* 1059 */       drawNodeText(this.selectedNode, (this.v.indexOf(this.selectedNode) - this.sbVPosition) * this.cellSize, true);
/* 1060 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean focusLost(Event e, Object what)
/*      */   {
/* 1065 */     this.hasFocus = false;
/* 1066 */     if ((this.selectedNode != null) && (this.v != null))
/* 1067 */       drawNodeText(this.selectedNode, (this.v.indexOf(this.selectedNode) - this.sbVPosition) * this.cellSize, true);
/* 1068 */     return true;
/*      */   }
/*      */ 
/*      */   protected void sendActionEvent()
/*      */   {
/* 1078 */     super.deliverEvent(new Event(this, 1001, this.selectedNode.getText()));
/*      */   }
/*      */ 
/*      */   public TreeNode getSelectedNode()
/*      */   {
/* 1087 */     return this.selectedNode;
/*      */   }
/*      */ 
/*      */   public String getSelectedText()
/*      */   {
/* 1097 */     if (this.selectedNode == null) {
/* 1098 */       return null;
/*      */     }
/* 1100 */     return this.selectedNode.getText();
/*      */   }
/*      */ 
/*      */   protected void changeSelection(TreeNode node)
/*      */   {
/* 1105 */     if (node == this.selectedNode) {
/* 1106 */       return;
/*      */     }
/* 1108 */     TreeNode oldNode = this.selectedNode;
/* 1109 */     this.selectedNode = node;
/* 1110 */     drawNodeText(oldNode, (this.v.indexOf(oldNode) - this.sbVPosition) * this.cellSize, true);
/* 1111 */     drawNodeText(node, (this.v.indexOf(node) - this.sbVPosition) * this.cellSize, true);
/*      */ 
/* 1114 */     int index = this.v.indexOf(this.selectedNode);
/*      */ 
/* 1124 */     if (index < this.sbVPosition)
/*      */     {
/* 1126 */       this.sbVPosition -= 1;
/* 1127 */       this.verticalScrollBar.setValue(this.sbVPosition);
/* 1128 */       triggerRedraw();
/* 1129 */       return;
/*      */     }
/*      */ 
/* 1132 */     if (index >= this.sbVPosition + (this.viewHeight - this.cellSize / 2) / this.cellSize)
/*      */     {
/* 1134 */       this.sbVPosition += 1;
/* 1135 */       this.verticalScrollBar.setValue(this.sbVPosition);
/* 1136 */       triggerRedraw();
/* 1137 */       return;
/*      */     }
/*      */ 
/* 1140 */     super.repaint();
/*      */   }
/*      */ 
/*      */   public synchronized void update(Graphics g)
/*      */   {
/* 1167 */     paint(g);
/*      */   }
/*      */ 
/*      */   public void paint(Graphics g)
/*      */   {
/* 1184 */     Dimension d = super.size();
/* 1185 */     if ((d.width != this.viewWidth) || (d.height != this.viewHeight)) {
/* 1186 */       triggerRedraw();
/*      */     }
/* 1188 */     if (this.redrawTriggered)
/*      */     {
/* 1191 */       redraw(g);
/*      */     }
/*      */ 
/* 1203 */     g.translate(-this.sbHPosition, 0);
/*      */ 
/* 1205 */     if ((this.sbVShow) && (this.sbHShow))
/*      */     {
/* 1207 */       g.setColor(Color.lightGray);
/* 1208 */       g.fillRect(this.sbHPosition + d.width - this.sbVWidth, d.height - this.sbHHeight, this.sbVWidth, this.sbHHeight);
/*      */     }
/* 1210 */     g.clipRect(this.sbHPosition, 0, d.width - this.sbVWidth, d.height - this.sbHHeight);
/* 1211 */     g.drawImage(this.im1, 0, 0, this);
/* 1212 */     g.setColor(Color.black);
/* 1213 */     g.drawRect(this.sbHPosition, 0, d.width - this.sbVWidth - 1, d.height - this.sbHHeight - 1);
/*      */   }
/*      */ 
/*      */   public void redraw()
/*      */   {
/* 1224 */     triggerRedraw();
/*      */   }
/*      */ 
/*      */   public void redraw(Graphics g)
/*      */   {
/* 1229 */     boolean recalculate = this.treeChanged;
/* 1230 */     int inRectCount = 0;
/*      */ 
/* 1232 */     this.redrawTriggered = false;
/* 1233 */     this.treeChanged = false;
/*      */ 
/* 1235 */     Dimension d = super.size();
/*      */ 
/* 1237 */     if (recalculate)
/*      */     {
/* 1239 */       resetVector();
/*      */ 
/* 1241 */       this.newWidth = compWidth(g);
/*      */ 
/* 1243 */       inRectCount = (d.height - this.sbHHeight) / this.cellSize;
/* 1244 */       if (this.viewCount > inRectCount)
/*      */       {
/* 1247 */         this.sbVShow = true;
/* 1248 */         this.sbVWidth = this.verticalScrollBar.preferredSize().width;
/*      */       }
/*      */       else
/*      */       {
/* 1252 */         this.sbVShow = false;
/* 1253 */         this.sbVWidth = 0;
/* 1254 */         this.sbVPosition = 0;
/*      */       }
/*      */ 
/* 1257 */       if (this.newWidth > d.width - this.sbVWidth)
/*      */       {
/* 1260 */         this.sbHShow = true;
/* 1261 */         this.sbHHeight = this.horizontalScrollBar.preferredSize().height;
/*      */       }
/*      */       else
/*      */       {
/* 1265 */         this.sbHShow = false;
/* 1266 */         this.sbHHeight = 0;
/* 1267 */         this.sbHPosition = 0;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1272 */     drawTree();
/*      */ 
/* 1274 */     if (recalculate) {
/* 1275 */       this.verticalScrollBar.setValues(this.sbVPosition, inRectCount, 0, this.viewCount - ((this.isSun1_1) ? 0 : inRectCount));
/* 1276 */       this.verticalScrollBar.setPageIncrement(inRectCount - 1);
/*      */ 
/* 1278 */       this.horizontalScrollBar.setValues(this.sbHPosition, d.width - this.sbVWidth, 0, this.sbHSize - ((this.isSun1_1) ? 0 : d.width - this.sbVWidth));
/* 1279 */       this.horizontalScrollBar.setPageIncrement(d.width - this.sbVWidth);
/* 1280 */       this.horizontalScrollBar.setLineIncrement(this.sbHLineIncrement);
/*      */ 
/* 1282 */       if (this.sbVShow)
/*      */       {
/* 1284 */         this.verticalScrollBar.reshape(d.width - this.sbVWidth, 0, this.sbVWidth, d.height - this.sbHHeight);
/* 1285 */         this.verticalScrollBar.show();
/*      */       }
/*      */       else
/*      */       {
/* 1289 */         this.verticalScrollBar.hide();
/*      */       }
/*      */ 
/* 1292 */       if (this.sbHShow)
/*      */       {
/* 1294 */         this.horizontalScrollBar.reshape(0, d.height - this.sbHHeight, d.width - this.sbVWidth, this.sbHHeight);
/* 1295 */         this.horizontalScrollBar.show();
/*      */       }
/*      */       else
/*      */       {
/* 1299 */         this.horizontalScrollBar.hide();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int compWidth(Graphics gg)
/*      */   {
/* 1306 */     int size = 0;
/*      */ 
/* 1310 */     Font f = super.getFont();
/*      */ 
/* 1312 */     if (f == null)
/*      */     {
/* 1314 */       f = new Font("TimesRoman", 0, 13);
/* 1315 */       gg.setFont(f);
/* 1316 */       super.setFont(f);
/*      */     }
/*      */ 
/* 1319 */     this.fm = gg.getFontMetrics();
/*      */ 
/* 1321 */     for (int i = 0; i < this.v.size(); ++i)
/*      */     {
/* 1323 */       TreeNode node = (TreeNode)this.v.elementAt(i);
/* 1324 */       int textOffset = (node.depth + 1) * this.cellSize + this.cellSize + this.textInset - ((node.getImage() == null) ? 12 : 0);
/* 1325 */       if (size < textOffset + this.fm.stringWidth(node.text) + 6) {
/* 1326 */         size = textOffset + this.fm.stringWidth(node.text) + 6;
/*      */       }
/*      */     }
/* 1329 */     return size;
/*      */   }
/*      */ 
/*      */   public void drawTree()
/*      */   {
/* 1336 */     Dimension d = super.size();
/*      */ 
/* 1338 */     if ((d.width != this.viewWidth) || (d.height != this.viewHeight) || (this.g1 == null) || (this.sbHSize != this.newWidth))
/*      */     {
/* 1341 */       this.im1 = super.createImage(Math.max(this.sbHSize = this.newWidth, d.width), d.height);
/* 1342 */       if (this.g1 != null) {
/* 1343 */         this.g1.dispose();
/*      */       }
/* 1345 */       this.g1 = this.im1.getGraphics();
/* 1346 */       this.viewWidth = d.width;
/* 1347 */       this.viewHeight = d.height;
/*      */     }
/*      */ 
/* 1350 */     Font f = super.getFont();
/*      */ 
/* 1353 */     if (f == null)
/*      */     {
/* 1355 */       f = new Font("TimesRoman", 0, 13);
/* 1356 */       this.g1.setFont(f);
/* 1357 */       super.setFont(f);
/*      */     }
/*      */ 
/* 1361 */     if ((f != null) && 
/* 1363 */       (this.g1.getFont() == null)) {
/* 1364 */       this.g1.setFont(f);
/*      */     }
/*      */ 
/* 1367 */     this.fm = this.g1.getFontMetrics();
/* 1368 */     this.g1.setColor(super.getBackground());
/* 1369 */     this.g1.fillRect(0, 0, this.im1.getWidth(this), d.height);
/*      */ 
/* 1372 */     int lastOne = this.sbVPosition + this.viewHeight / this.cellSize + 1;
/*      */ 
/* 1374 */     if (lastOne > this.viewCount)
/*      */     {
/* 1376 */       lastOne = this.viewCount;
/*      */     }
/*      */ 
/* 1379 */     TreeNode outerNode = null;
/* 1380 */     if (!this.v.isEmpty())
/* 1381 */       outerNode = (TreeNode)this.v.elementAt(this.sbVPosition);
/* 1382 */     for (int i = this.sbVPosition; i < lastOne; ++i)
/*      */     {
/* 1384 */       TreeNode node = (TreeNode)this.v.elementAt(i);
/* 1385 */       int x = this.cellSize * (node.depth + 1);
/* 1386 */       int y = (i - this.sbVPosition) * this.cellSize;
/*      */ 
/* 1389 */       this.g1.setColor(super.getForeground());
/*      */ 
/* 1392 */       if (node.sibling != null)
/*      */       {
/* 1394 */         int k = this.v.indexOf(node.sibling) - i;
/*      */ 
/* 1396 */         if (k > lastOne)
/*      */         {
/* 1398 */           k = lastOne;
/*      */         }
/*      */ 
/* 1401 */         drawDotLine(x - this.cellSize / 2, y + this.cellSize / 2, x - this.cellSize / 2, y + this.cellSize / 2 + k * this.cellSize);
/*      */       }
/*      */ 
/* 1406 */       for (int m = 0; m < i; ++m)
/*      */       {
/* 1408 */         TreeNode sib = (TreeNode)this.v.elementAt(m);
/*      */ 
/* 1410 */         if ((sib.sibling != node) || (m >= this.sbVPosition))
/*      */           continue;
/* 1412 */         drawDotLine(x - this.cellSize / 2, 0, x - this.cellSize / 2, y + this.cellSize / 2);
/*      */       }
/*      */ 
/* 1418 */       if (node.isExpanded())
/*      */       {
/* 1420 */         drawDotLine(x + this.cellSize / 2, y + this.cellSize - 2, x + this.cellSize / 2, y + this.cellSize + this.cellSize / 2);
/*      */       }
/*      */ 
/* 1424 */       this.g1.setColor(super.getForeground());
/* 1425 */       drawDotLine(x - this.cellSize / 2, y + this.cellSize / 2, x + this.cellSize / 2, y + this.cellSize / 2);
/*      */ 
/* 1429 */       if (node.isExpandable())
/*      */       {
/* 1431 */         this.g1.setColor(super.getBackground());
/* 1432 */         this.g1.fillRect(this.cellSize * node.depth + this.cellSize / 4, y + this.clickSize / 2, this.clickSize, this.clickSize);
/* 1433 */         this.g1.setColor(super.getForeground());
/* 1434 */         this.g1.drawRect(this.cellSize * node.depth + this.cellSize / 4, y + this.clickSize / 2, this.clickSize, this.clickSize);
/*      */ 
/* 1436 */         this.g1.drawLine(this.cellSize * node.depth + this.cellSize / 4 + 2, y + this.cellSize / 2, this.cellSize * node.depth + this.cellSize / 4 + this.clickSize - 2, y + this.cellSize / 2);
/*      */ 
/* 1439 */         if (!node.isExpanded())
/*      */         {
/* 1441 */           this.g1.drawLine(this.cellSize * node.depth + this.cellSize / 2, y + this.clickSize / 2 + 2, this.cellSize * node.depth + this.cellSize / 2, y + this.clickSize / 2 + this.clickSize - 2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1447 */       Image nodeImage = node.getImage();
/*      */ 
/* 1449 */       if (nodeImage != null)
/*      */       {
/* 1451 */         this.g1.drawImage(nodeImage, x + this.imageInset, y, this);
/*      */       }
/*      */ 
/* 1455 */       if (node.text != null)
/*      */       {
/* 1457 */         drawNodeText(node, y, true);
/*      */       }
/*      */ 
/* 1460 */       if (outerNode.depth > node.depth) {
/* 1461 */         outerNode = node;
/*      */       }
/*      */     }
/*      */ 
/* 1465 */     if (outerNode == null)
/*      */       return;
/* 1467 */     while ((outerNode = outerNode.parent) != null)
/*      */     {
/* 1469 */       if (outerNode.sibling != null)
/* 1470 */         drawDotLine(this.cellSize * (outerNode.depth + 1) - this.cellSize / 2, 0, this.cellSize * (outerNode.depth + 1) - this.cellSize / 2, d.height);
/*      */     }
/*      */   }
/*      */ 
/*      */   void drawNodeText(TreeNode node, int yPosition, boolean eraseBackground)
/*      */   {
/* 1479 */     int depth = node.depth;
/* 1480 */     Image nodeImage = node.getImage();
/* 1481 */     int textOffset = (depth + 1) * this.cellSize + this.cellSize + this.textInset - ((nodeImage == null) ? 12 : 0);
/*      */     Color bg = null;
/* 1501 */     if (node.color != null) {
/* 1502 */       Color fg = (isLight(node.color)) ? Color.black : Color.white;
/* 1503 */       Color localColor1 = node.color;
/*      */     }
/*      */     else {
/* 1506 */       Color fg = super.getForeground();
/* 1507 */       bg = super.getBackground();
/*      */     }
/*      */     Color fg=null;
/* 1510 */     if (eraseBackground)
/*      */     {
/* 1512 */       this.g1.setColor(bg);
/* 1513 */       this.g1.fillRect(textOffset - 1, yPosition + 1, this.fm.stringWidth(node.text) + 4, this.cellSize - 1);
/*      */     }
/*      */ 
/* 1516 */     if (node == this.selectedNode)
/*      */     {
/* 1518 */       this.g1.setColor(super.getForeground());
/* 1519 */       this.g1.drawRect(textOffset - 1, yPosition + 1, this.fm.stringWidth(node.text) + 3, this.cellSize - 2);
/* 1520 */       super.repaint(Math.max(0, textOffset - 1 - this.sbHPosition), yPosition + 1, this.fm.stringWidth(node.text) + 4, this.cellSize - 1);
/*      */     }
/* 1522 */     this.g1.setColor(fg);
/* 1523 */     this.g1.drawString(node.text, textOffset, yPosition + this.cellSize - this.textBaseLine);
/*      */   }
/*      */ 
/*      */   private boolean isLight(Color c) {
/* 1527 */     int r = c.getRed();
/* 1528 */     int g = c.getGreen();
/* 1529 */     int b = c.getBlue();
/*      */ 
/* 1531 */     int min = (r < g) ? r : g;
/* 1532 */     if (b < min) {
/* 1533 */       min = b;
/*      */     }
/* 1535 */     return min > 128;
/*      */   }
/*      */ 
/*      */   private void drawDotLine(int x0, int y0, int x1, int y1)
/*      */   {
/* 1540 */     if (y0 == y1)
/*      */     {
/* 1542 */       for (int i = x0; i < x1; i += 2)
/*      */       {
/* 1544 */         this.g1.drawLine(i, y0, i, y1);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/* 1549 */       for (int i = y0; i < y1; i += 2)
/*      */       {
/* 1551 */         this.g1.drawLine(x0, i, x1, i);
/*      */       }
/*      */   }
/*      */ 
/*      */   private void parseTreeStructure(String[] tempStructure)
/*      */     throws InvalidTreeNodeException
/*      */   {
/* 1559 */     for (int i = 0; i < tempStructure.length; ++i)
/*      */     {
/* 1561 */       String entry = tempStructure[i];
/* 1562 */       int indentLevel = findLastPreSpace(entry);
/*      */ 
/* 1564 */       if (indentLevel == -1) {
/* 1565 */         throw new InvalidTreeNodeException();
/*      */       }
/* 1567 */       TreeNode node = new TreeNode(entry.trim());
/* 1568 */       node.setDepth(indentLevel);
/*      */ 
/* 1570 */       if (this.rootNode == null)
/*      */       {
/* 1572 */         if (indentLevel != 0) {
/* 1573 */           throw new InvalidTreeNodeException();
/*      */         }
/* 1575 */         append(node);
/*      */       }
/*      */       else
/*      */       {
/* 1579 */         TreeNode currentNode = this.rootNode;
/* 1580 */         while (currentNode.sibling != null) {
/* 1581 */           currentNode = currentNode.sibling;
/*      */         }
/* 1583 */         for (int j = 1; j < indentLevel; ++j)
/*      */         {
/* 1585 */           int numberOfChildren = currentNode.numberOfChildren;
/* 1586 */           TreeNode tempNode = null;
/*      */ 
/* 1588 */           if (numberOfChildren > 0)
/*      */           {
/* 1590 */             tempNode = currentNode.child;
/*      */ 
/* 1592 */             while (tempNode.sibling != null) {
/* 1593 */               tempNode = tempNode.sibling;
/*      */             }
/*      */           }
/* 1596 */           if (tempNode != null)
/* 1597 */             currentNode = tempNode;
/*      */           else {
/* 1599 */             break;
/*      */           }
/*      */         }
/* 1602 */         int diff = indentLevel - currentNode.getDepth();
/*      */ 
/* 1604 */         if (diff > 1) {
/* 1605 */           throw new InvalidTreeNodeException();
/*      */         }
/* 1607 */         if (diff == 1)
/* 1608 */           insert(node, currentNode, 0);
/*      */         else
/* 1610 */           insert(node, currentNode, 1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int findLastPreSpace(String s)
/*      */   {
/* 1619 */     int length = s.length();
/*      */ 
/* 1621 */     if ((s.charAt(0) != ' ') && (s.charAt(0) != '\t'))
/*      */     {
/* 1623 */       return 0;
/*      */     }
/*      */ 
/* 1626 */     for (int i = 1; i < length; ++i)
/*      */     {
/* 1628 */       if ((s.charAt(i) != ' ') && (s.charAt(i) != '\t'))
/*      */       {
/* 1630 */         return i;
/*      */       }
/*      */     }
/*      */ 
/* 1634 */     return -1;
/*      */   }
/*      */ 
/*      */   public synchronized Dimension preferredSize()
/*      */   {
/* 1647 */     return new Dimension(175, 125);
/*      */   }
/*      */ 
/*      */   public synchronized Dimension minimumSize()
/*      */   {
/* 1659 */     return new Dimension(50, 50);
/*      */   }
/*      */ 
/*      */   public void setLayout(LayoutManager lm)
/*      */   {
/*      */   }
/*      */ 
/*      */   void scrolled()
/*      */   {
/* 1785 */     this.redrawTriggered = true;
/* 1786 */     super.repaint();
/*      */   }
/*      */ 
/*      */   protected void triggerRedraw()
/*      */   {
/* 1791 */     this.redrawTriggered = true;
/* 1792 */     this.treeChanged = true;
/* 1793 */     super.repaint();
/*      */   }
/*      */ 
/*      */   public TreeView()
/*      */   {
/*  164 */     this.sbVPosition = 0; this.sbVTimer = -1L; this.sbVShow = false; this.count = 0; this.viewCount = 0; this.sbHPosition = 0; this.sbHHeight = 0; this.newWidth = 0; this.sbHShow = false; this.sbHLineIncrement = 4; this.viewHeight = 300; this.viewWidth = 300; this.viewWidest = 0; this.cellSize = 16; this.clickSize = 8; this.imageInset = 3; this.textInset = 6; this.textBaseLine = 3; this.g1 = null; this.redrawTriggered = false; this.treeChanged = false; this.hasFocus = false;
/*  165 */     super.setLayout(null);
/*  166 */     this.verticalScrollBar = new Scrollbar(1);
/*  167 */     this.verticalScrollBar.hide();
/*  168 */     super.add(this.verticalScrollBar);
/*  169 */     this.horizontalScrollBar = new Scrollbar(0);
/*  170 */     this.horizontalScrollBar.hide();
/*  171 */     super.add(this.horizontalScrollBar);
/*      */ 
/*  176 */     this.isSun1_1 = true;
/*      */   }
/*      */ 
/*      */   public TreeView(TreeNode head)
/*      */   {
/*  187 */     this.selectedNode = (this.rootNode = head);
/*  188 */     this.count = 1;
/*      */   }
/*      */ }

/* Location:           C:\Users\Administrator.FS-PC\Desktop\websphinx\lib\
 * Qualified Name:     symantec.itools.awt.TreeView
 * JD-Core Version:    0.5.4
 */