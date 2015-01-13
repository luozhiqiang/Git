/*     */ package symantec.itools.awt;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Image;
/*     */ 
/*     */ public class TreeNode
/*     */ {
/*     */   TreeNode sibling;
/*     */   TreeNode child;
/*     */   TreeNode parent;
/*     */   String text;
/*     */   Color color;
/*     */   Image collapsedImage;
/*     */   Image expandedImage;
/*     */   int numberOfChildren;
/*     */   Object dataObject;
/*     */   int depth;
/*     */   boolean isExpanded;
/*     */ 
/*     */   void setDepth(int depth)
/*     */   {
/*  70 */     this.depth = depth;
/*     */   }
/*     */ 
/*     */   public int getDepth()
/*     */   {
/*  79 */     return this.depth;
/*     */   }
/*     */ 
/*     */   public void setColor(Color color)
/*     */   {
/*  89 */     this.color = color;
/*     */   }
/*     */ 
/*     */   public Color getColor()
/*     */   {
/*  98 */     return this.color;
/*     */   }
/*     */ 
/*     */   public boolean isExpanded()
/*     */   {
/* 108 */     return this.isExpanded;
/*     */   }
/*     */ 
/*     */   public boolean isExpandable()
/*     */   {
/* 118 */     return this.child != null;
/*     */   }
/*     */ 
/*     */   public void expand()
/*     */   {
/* 126 */     if (!isExpandable())
/*     */       return;
/* 128 */     this.isExpanded = true;
/*     */   }
/*     */ 
/*     */   public void collapse()
/*     */   {
/* 137 */     this.isExpanded = false;
/*     */   }
/*     */ 
/*     */   public void toggle()
/*     */   {
/* 146 */     if (this.isExpanded)
/*     */     {
/* 148 */       collapse();
/*     */     } else {
/* 150 */       if (!isExpandable())
/*     */         return;
/* 152 */       expand();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Image getImage()
/*     */   {
/* 162 */     return ((this.isExpanded) && (this.expandedImage != null)) ? this.expandedImage : this.collapsedImage;
/*     */   }
/*     */ 
/*     */   public void setExpandedImage(Image image)
/*     */   {
/* 175 */     this.expandedImage = image;
/*     */   }
/*     */ 
/*     */   public void setCollapsedImage(Image image)
/*     */   {
/* 186 */     this.collapsedImage = image;
/*     */   }
/*     */ 
/*     */   public String getText()
/*     */   {
/* 196 */     return this.text;
/*     */   }
/*     */ 
/*     */   public void setText(String s)
/*     */   {
/* 206 */     this.text = new String(s);
/*     */   }
/*     */ 
/*     */   public Object getDataObject()
/*     */   {
/* 217 */     return this.dataObject;
/*     */   }
/*     */ 
/*     */   public void setDataObject(Object theObject)
/*     */   {
/* 228 */     this.dataObject = theObject;
/*     */   }
/*     */ 
/*     */   public TreeNode getParent()
/*     */   {
/* 239 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public TreeNode getChild()
/*     */   {
/* 250 */     return this.child;
/*     */   }
/*     */ 
/*     */   public TreeNode getSibling()
/*     */   {
/* 261 */     return this.sibling;
/*     */   }
/*     */ 
/*     */   public TreeNode(String text)
/*     */   {
/*  39 */     this(text, null, null);
/*     */   }
/*     */ 
/*     */   public TreeNode(String text, Image collapsedImage, Image expandedImage)
/*     */   {
/*  52 */     this.depth = -1; this.isExpanded = false;
/*  53 */     this.text = text;
/*  54 */     this.color = null;
/*  55 */     this.sibling = null;
/*  56 */     this.child = null;
/*  57 */     this.collapsedImage = collapsedImage;
/*  58 */     this.expandedImage = expandedImage;
/*  59 */     this.numberOfChildren = 0;
/*  60 */     this.dataObject = null;
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator.FS-PC\Desktop\websphinx\lib\
 * Qualified Name:     symantec.itools.awt.TreeNode
 * JD-Core Version:    0.5.4
 */