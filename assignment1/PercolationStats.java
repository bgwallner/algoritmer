import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private double mean = 0;
    private double stddev = 0;
    private int t = 0;
   
     // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        
        if (n <= 0 || trials <= 0) 
            throw new IllegalArgumentException("index out of bounds");
        
        boolean percolates = false;
        int nbrOpened = 0;
        
        t = trials;
        
        double[] meanArray = new double[t];
        
        for (int index = 0; index < t; index++) {
            /* Create Pecolation object */
            Percolation perc = new Percolation(n);
            /* Start opening sites */
            nbrOpened = 0;
            percolates = false;
            while (!percolates) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if (!perc.isOpen(row, col)) {
                    /* Open site */
                    perc.open(row, col);
                    nbrOpened++;
                    /* Check if system percolates */
                    if (perc.percolates())
                        percolates = true;
                }
            }
            /* Store into array */
            meanArray[index] = (double) nbrOpened/(double) (n*n);
        }
        
        /* Get mean based on meanArray data */
        mean = StdStats.mean(meanArray);
        
        /* Get standard deviation */
        stddev = StdStats.stddev(meanArray);
    }
   
   // sample mean of percolation threshold
   public double mean() {
       return mean;
   }
   
   // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }
    
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mean-1.96*stddev/Math.sqrt(t));
    }
   
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean+1.96*stddev/Math.sqrt(t));
    }
    
    // test client (described below)
    public static void main(String[] args) {
        
        System.out.printf("Starting PercolationStats\n");
        
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        
        if (n <= 0 || trials <= 0) 
            throw new IllegalArgumentException("index out of bounds");
        
        PercolationStats percStat = new PercolationStats(n, trials);
        
        System.out.printf("mean                            %f\n", percStat.mean());
        System.out.printf("stddev                          %f\n", percStat.stddev());
        System.out.printf("95 percent confidence interval  %f, %f\n", 
                          percStat.confidenceLo(), percStat.confidenceHi());
        
        
    }
}
