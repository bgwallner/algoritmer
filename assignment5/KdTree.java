import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

/* TODO: put() and get() should have boolean in interface instead */
/* of the depth. However, since each of them does not work in     */
/* parallell braches during recursion this will work but stinks   */
/* like a brown pile...                                           */

public class KdTree {
    
    private Node root;             // root of BST
    private int size;
    private int depth;
    
    /* define a node */
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
    }
    
    /************** Private helper methods ***********/

    /* put method from BST.java */
     private Node put(Node x, Point2D p, double xLow, 
                      double xHigh, double yLow, double yHigh) {
         
         /* Create a new node if null is reached */
         if (x == null) {
             Node y = new Node();
             y.p = new Point2D(p.x(), p.y());
             y.rect = new RectHV(xLow, yLow, xHigh, yHigh);
             y.lb = null;
             y.rt = null;
             size++;
             return y;
         }
         
         /* Check if the same point, then just return Node x */
         if (cmpHorizontal(x,p) == 0 && cmpVertical(x,p) == 0)
             return x;
                      
         /* Check if current depth is odd or even */
         /* Depth is for the _current_ Node x     */
         if (depth % 2 == 0) {
             if (cmpHorizontal(x,p)  < 0) {
                 depth++;
                 x.lb  = put(x.lb, p, xLow, x.p.x(), yLow, yHigh);
             }
             else {
                 depth++;
                 x.rt = put(x.rt, p, x.p.x(), xHigh, yLow, yHigh);
             }
         }
         else {
             if (cmpVertical(x,p)  < 0) {
                 depth++;
                 x.lb  = put(x.lb, p, xLow, xHigh, yLow, x.p.y());
             }
             else {
                 depth++;
                 x.rt = put(x.rt, p, xLow, xHigh, x.p.y(), yHigh);
             }
         }
         return x;
     }

    
    /* get method from BST lecture notes - use the simpler while version */
    private boolean get(Point2D p) {
        
        Node x = root;
        /* Traverse tree until found */
        while (x != null) {
            /* If even, compare p.x and if odd p.y.     */
            /* If less, go left and if larger go right. */
            /* If equal, return.                        */
            
            /* Check if the same point, then just return Node x */
            if (cmpHorizontal(x,p) == 0 && cmpVertical(x,p) == 0)
                return true;
            
            /* Check if even, then compare 'x' */
            /* otherwise, compare y.           */
            if (depth % 2 == 0) {
                /* Check if found, return or go to next node */
                if (cmpHorizontal(x,p)  < 0)  x = x.lb;    
                else                          x = x.rt;
                depth++;
            }
            else {
                /* Check if found, return or go to next node */
                if (cmpVertical(x,p)  < 0)    x = x.lb;    
                else                          x = x.rt;
                depth++;
            }
        }
        return false;
    }
    
    /* Compare 'x' coordinate of node with that of p */
    private int cmpHorizontal(Node node, Point2D p) {
       /* Compare x-coordinate of p */
       if (p.x() < node.p.x())      return -1;
       else if (p.x() > node.p.x()) return 1;
       else                         return 0;
    }
    
    /* Compare 'y' coordinate of node with that of p */
    private int cmpVertical(Node node, Point2D p) {
       /* Compare y-coordinate of p */
       if (p.y() < node.p.y())      return -1;
       else if (p.y() > node.p.y()) return 1;
       else                         return 0;
    }
    
    /* Recursive helper method for draw() */
    private void draw(Node x, boolean drawHorizontal) {
        if (x == null) return;
        
        /* Draw point */
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();

        /* Draw line */
        if (!drawHorizontal) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        /* Do recusion left and right */
        draw(x.lb, !drawHorizontal);
        draw(x.rt, !drawHorizontal);
    }
    
    /* Private recursive helper method for adding all _one_ point to 'q'    */
    /* if contained in 'rect'. Then recursively work it's way down the tree */
    private void rangeRecursive(Node node, RectHV rect, Queue<Point2D> q) {
        if (node == null) return;
        /* Add if rectange contains node point */
        if (rect.contains(node.p)) {
            q.enqueue(node.p);
        }
        /* Only work it's way down if intersection */
        if (rect.intersects(node.rect)) {
            rangeRecursive(node.lb, rect, q);
            rangeRecursive(node.rt, rect, q);
        }
    }
    
    /***************** Public interface **************/
    
    /* Construct an empty set of points */
    public KdTree() {
        root = null;
        size = 0;
    }
    
    /* Is the set empty? */ 
    public boolean isEmpty() {
        return (size == 0);
    }
    
    /* Number of points in the set */
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
         if (p == null) throw new IllegalArgumentException("p == null");
        /* Call private helper method */
        depth = 0; /* Somewhat ugly... could be in 'put' but... never mind */
        root = put(root, p, 0.0, 1.0, 0.0, 1.0);
    }
    
    /* Does the set contain point p? */
    public boolean contains(Point2D p) {
         if (p == null) throw new IllegalArgumentException("p == null");
        /* Call private helper method */
        depth = 0;
        return get(p);
    }
    
    /* Draw all points to standard draw */
    public void draw() {
        /* Use private method in analogy with insert    */
        /* Must pass 'level' parameter since working in */
        /* both subtrees recursively.                   */
        
        /* Should probably also use boolean in 'put' etc... */
        draw(root, false);
    }
    
    /* All points that are inside the rectangle  */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("rect == null");
        /* Create an iterable */
        Queue<Point2D> queue = new Queue<Point2D>();
        rangeRecursive(root, rect, queue);
        return queue;
    }
    
    /* A nearest neighbor in the set to point p; null if the set is empty */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("p == null");
        if (size() == 0) return null;
        /* Work recursively down */
        return nearestRecursive(root, p, root.p, false);
    }
    
    /* Recursive helper method for working down both subtrees finding */
    /* nearest neighbor (isHorizontal = compare y                     */
    private Point2D nearestRecursive(Node x, Point2D p, 
                                     Point2D c, boolean isHorizontal) {
        
        Point2D nearestPoint = c;
        
        /* No more points, return nearestPoint */
        if (x == null) return nearestPoint;
        
        /* Compare the point in x to p and the nearest to p */ 
        if (x.p.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p))
            nearestPoint = x.p;
        
        /* Is current rectangle closer to p than the closest point?   */
        /* Compare query point with rectangle corresponding to Node x */
        /* (see 'Geometric primitives'                                */
        if (x.rect.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
            /* Left or right sub-tree ? */
            Node a;
            Node b;
            if ((!isHorizontal && (p.x() < x.p.x()))) {
                a = x.lb;
                b = x.rt;
            }
            else if ((isHorizontal && (p.y() < x.p.y()))) {
                a = x.lb;
                b = x.rt;
            }
            else {
                a = x.rt;
                b = x.lb;
            }
            
            /* Go down recursively in both nodes, first the one that is on */
            /* the same side of the splitting line                         */
            nearestPoint = nearestRecursive(a, p, nearestPoint, !isHorizontal);
            nearestPoint = nearestRecursive(b, p, nearestPoint, !isHorizontal);
        }
        return nearestPoint;
    }

    /* Unit testing of the methods (optional) */ 
    public static void main(String[] args) {
        
        /* Create a new KdTree */
        KdTree kdtree = new KdTree();
        
        /* Create a number of points */
        Point2D p1 = new Point2D(0.33, 0.44);
        Point2D p2 = new Point2D(1.0, 1.0);
        Point2D p3 = new Point2D(0.5, 0.9);
        Point2D p4 = new Point2D(0.1, 0.2);
        Point2D p5 = new Point2D(0.231, 0.731);
        Point2D p6 = new Point2D(0.5234, 0.3527);
        Point2D p7 = new Point2D(0.534, 0.323337);
        Point2D p8 = new Point2D(0.54, 0.37);
        Point2D p9 = new Point2D(0.5254, 0.35627);
        Point2D p10 = new Point2D(0.534, 0.327);
        Point2D p11 = new Point2D(0.52349, 0.356627);
        
        /* Add points to the tree */
        kdtree.insert(p1);
        kdtree.insert(p3);
        kdtree.insert(p5);
        kdtree.insert(p7);
        kdtree.insert(p9);
        kdtree.insert(p11);
        
        StdOut.println(kdtree.contains(p1));
        StdOut.println(kdtree.contains(p2));
        StdOut.println(kdtree.contains(p3));
        StdOut.println(kdtree.contains(p4));
        StdOut.println(kdtree.contains(p5));
        StdOut.println(kdtree.contains(p6));
        StdOut.println(kdtree.contains(p7));
        StdOut.println(kdtree.contains(p8));
        StdOut.println(kdtree.contains(p9));
        StdOut.println(kdtree.contains(p10));
        StdOut.println(kdtree.contains(p11));
        
        kdtree.draw();
    }
}