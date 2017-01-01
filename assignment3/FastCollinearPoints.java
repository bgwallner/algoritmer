import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import java.lang.*;
import java.util.*;

public class FastCollinearPoints {
    
    /* Create an ArrayList since size of segments unknown */
    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
    
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException("points = null");
        
        /* Number of points */
        int length = points.length;
        
        /* Check for multiple entries */
        for (int i=0; i<length-1; i++) {
            for (int j=i+1; j<length; j++) {
                if (points[i].equals(points[j]))
                   throw new IllegalArgumentException("repeated point"); 
            }
            
        }
        
        /* Create reference array */
        Point[] pointsCopy = Arrays.copyOf(points, length);
        
        /* Sort original array in natural order */
        Arrays.sort(points);
        
        /* for every point do */
        for (int i=0; i<length; i++) {
            Point p = pointsCopy[i];
            /* Now sort the array with in order of the slope that all other  */
            /* points make with p. When comparision is made to p with itself */
            /* zero is returned.                                             */
            Arrays.sort(points, 0, length, p.slopeOrder());
            
            /********** DEBUG *************/
            /* Create an empty array with slopes */
            //int slopesLength = length-1;
            //double[] slopes = new double[slopesLength];
        
            /* Calculate slope that points[0] makes with every other point */
            //for (int k=0; k<slopesLength; k++) {
            //    slopes[k] = p.slopeTo(points[k+1]);
            //}
            //System.out.println(Arrays.toString(slopes));
            /********** DEBUG *************/

            /* Find indexLow and indexLow in points with at least */
            /* 4 ajacent points with the same slope               */
            int indexLow = 1;
            int indexHigh = 2;
            /* Check that p is smallest */
            boolean smallestCond = p.compareTo(points[indexLow]) < 0 ? true : false;
            
            while (indexHigh < length) {
                    /* Check if slopes high and low has the same slope to p */
                    if (Double.compare(points[indexLow].slopeTo(p),
                                       points[indexHigh].slopeTo(p)) == 0) {
                        
                        /* Must be larger than p=points[i] */
                        if (points[indexHigh].compareTo(p) < 0)
                            smallestCond = false;
                    }
                    /* Condition for ajacent points not fullfilled any longer */
                    else {
                        /* Check if found base + 3 new points */
                        if (smallestCond && indexHigh - indexLow >= 3) {
                            /* Add points[indexLow...indexHigh] to segment */
                            Arrays.sort(points, indexLow, indexHigh);
                            LineSegment newSegment = new LineSegment(points[0], points[indexHigh-1]);
                            segments.add(newSegment);
                        }
                        
                        /* New low index is high index */
                        indexLow = indexHigh;
                        /* Check condition when indexLow has changed */
                        smallestCond = p.compareTo(points[indexLow]) < 0 ? true : false;
                    }
                    indexHigh++;
            }
            /* Check boundary */
            if (points[length-1].slopeTo(p) == points[indexLow].slopeTo(p)) {
                if (smallestCond && indexHigh - indexLow >= 3) {
                    Arrays.sort(points, indexLow, indexHigh);
                    LineSegment newSegment = new LineSegment(points[0], points[indexHigh-1]);
                    segments.add(newSegment);
                }
            }
        }
    }
    
    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }
    
    // the line segments
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
    
}