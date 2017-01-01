import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import java.lang.*;
import java.util.*;

public class BruteCollinearPoints {

    /* Create an ArrayList since size of segments unknown */
    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
    
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException("points = null");
        
        int length = points.length;
        
        /* Check for multiple entries */
        for (int i=0; i<length-1; i++) {
            for (int j=i+1; j<length; j++) {
                if (points[i].equals(points[j]))
                   throw new IllegalArgumentException("repeated point"); 
            }
            
        }
        
        
        /* Copy points and sort it to get last index and first  */
        /* as far away from each other as possible. Otherwise   */
        /* linesegment can be shorter than it really is. Could  */
        /* do this as a comparison when (i,j,k,l) are found but */
        /* this is easier.                                      */
        Point[] pointsSorted = Arrays.copyOf(points, length);
        Arrays.sort(pointsSorted);
        
        for (int i=0; i<length-3; i++) {
            for (int j=i+1; j<length-2; j++) {
                
                /* Check for null */
                if (pointsSorted[i] == null || pointsSorted[j] == null)
                    throw new NullPointerException("points = null");
                
                /* Start from 0 and work through n^4 */
                for (int k=j+1; k<length-1; k++) {
                    
                    /* Check for null */
                    if (pointsSorted[k] == null)
                        throw new NullPointerException("points = null");
                    
                    for (int l=k+1; l<length; l++) {
                        
                        /* Check for null */
                        if (pointsSorted[l] == null)
                            throw new NullPointerException("points = null");
                        
                            if (Double.compare(pointsSorted[i].slopeTo(pointsSorted[j]),
                                               pointsSorted[j].slopeTo(pointsSorted[k])) == 0 &&
                                Double.compare(pointsSorted[j].slopeTo(pointsSorted[k]),
                                               pointsSorted[k].slopeTo(pointsSorted[l])) == 0) {
                                /* Found 4 collinear points, add (i,l) to 'segments' */
                                LineSegment newSegment = new LineSegment(pointsSorted[i],pointsSorted[l]);
                                segments.add(newSegment);
                            }  
                    }
                }
            }
        }
    }
    
    /* return number of segments */
    public int numberOfSegments() {
        return segments.size();
    }
    
    /* return array of linesegments */
    public LineSegment[] segments() {
        LineSegment[] a = new LineSegment[segments.size()];
        /* Convert ArrayList<LineSegment> to array */
        segments.toArray(a);
        return a;
    }
    
    public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
        int x = in.readInt();
        int y = in.readInt();
        points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
        p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
        StdOut.println(segment);
        segment.draw();
    }
    StdDraw.show();
}
}