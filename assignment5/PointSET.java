import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;

public class PointSET {

    /* Use SET for representing red-black BST */
    private SET<Point2D> pointSet2D;

    // construct an empty set of points
    public PointSET() {
        pointSet2D = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet2D.isEmpty();
    }

    // number of points in the set
    public int size() {
        /* Return the size */
        return pointSet2D.size();
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        /* Check argument */
        if (p == null)
            throw new NullPointerException("p = null");
        /* Add p with add() */
        pointSet2D.add(p);
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        /* Check argument */
        if (p == null)
            throw new NullPointerException("p = null");
        
        /* Return if set contains p with contains() */
        return pointSet2D.contains(p);
    }

    // draw all of the points to standard draw
    public void draw() {
        /* Go through all points in pointSet2D and draw them */
        for (Point2D point : pointSet2D) {
            point.draw();
        }
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        /* Create a queue for storing points within rect */
        Queue<Point2D> queue = new Queue<Point2D>();
        /* Iterate through all points in pointSet2D */
        for (Point2D iterator : pointSet2D) {
            /* Check if point is within rect */
            if (rect.contains(iterator))
                queue.enqueue(iterator);
        }
        return queue;
    }

    // a nearest neighbor in the set to p; null if the set is empty
    public Point2D nearest(Point2D p) {
        /* Check argument */
        if (p == null)
            throw new NullPointerException("p = null");
        
        Point2D champion = null;
        /* Go through pointSet2D */
        for (Point2D iterator : pointSet2D) {
            if (champion == null || iterator.distanceSquaredTo(p) < 
                                    champion.distanceSquaredTo(p)) {
                champion = iterator;
            }
        }
        return champion;
    }
    
    // unit testing of the methods (optional)
    public static void main(String[] args) {
        
    }
}