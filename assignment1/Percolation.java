import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    /* Declare grid */
    private int[][] percModel;
    private int size;
    private WeightedQuickUnionUF uf;
    
    // Create n-by-n grid Percolation model, with all sites blocked
    public Percolation(int n) {
        
        if (n <= 0)
            throw new IllegalArgumentException("index must be larger than 0");
        
        /* Set n to internal size variable. */
        size = n;
        
        /* Init Percolation modell to 'blocked'. */
        percModel = new int[size][size];    
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
              percModel[i][j] = 1; // Initialize all to 'blocked'
        
        /* Create instance of WeightedQuickUnionUF */
        
        /* Connection model is n^2+2 including first/last as virtual */
        /* levels Array is then 0->size^2+1.                         */
        uf = new WeightedQuickUnionUF(size*size+2);
    }

   // Open site (row, col) if it is not open already
    public void open(int row, int col) {
        
        if (row <= 0 || row > size)
            throw new IndexOutOfBoundsException("row index out of bounds");
        if (col <= 0 || col > size)
            throw new IndexOutOfBoundsException("col index out of bounds");
        
        /* Internally index = 0->n-1 */
        int rowIndex = row - 1;
        int colIndex = col - 1;
        
        /* Open site in Percolation model (0->n-1) */
        percModel[rowIndex][colIndex] = 0;
        
        /* Map Percolation model to Connection model */
        
        /* index is 1->n^2 */
        int index = size*(row - 1) + col;
        
        /* Connect to all ajacent sites */
        if (rowIndex == 0) {
            
            /* Upper left corner */
            if (colIndex == 0) {
                if (percModel[rowIndex][colIndex+1] == 0)
                    uf.union(index, index+1);
                if (percModel[rowIndex+1][colIndex] == 0)
                    uf.union(index, index+size);
            }
            /* Upper right corner */
            else if (colIndex == size-1) {
                if (percModel[rowIndex][colIndex-1] == 0)
                    uf.union(index, index-1);
                if (percModel[rowIndex+1][colIndex] == 0)
                    uf.union(index, index+size);    
            }
            /* Mid first row */
            else {
                if (percModel[rowIndex][colIndex-1] == 0)
                    uf.union(index, index-1);
                if (percModel[rowIndex][colIndex+1] == 0)
                    uf.union(index, index+1);
                if (percModel[rowIndex+1][colIndex] == 0)
                    uf.union(index, index+size);
            }
            
            /* Connect to top virtual level */
            uf.union(index, 0);
        }
        else if (rowIndex == size-1) {
            
            /* Lower left corder */
            if (colIndex == 0) {
                if (percModel[rowIndex][colIndex+1] == 0)
                    uf.union(index, index+1);
                if (percModel[rowIndex-1][colIndex] == 0)
                    uf.union(index, index-size);
            }
            /* Lower right corner */
            else if (colIndex == size-1) {
                if (percModel[rowIndex][colIndex-1] == 0)
                    uf.union(index, index-1);
                if (percModel[rowIndex-1][colIndex] == 0)
                    uf.union(index, index-size);    
            }
            /* Mid lower row */
            else {
                if (percModel[rowIndex][colIndex-1] == 0)
                    uf.union(index, index-1);
                if (percModel[rowIndex][colIndex+1] == 0)
                    uf.union(index, index+1);
                if (percModel[rowIndex-1][colIndex] == 0)
                    uf.union(index, index-size);
            }
            
            /* Connect to low virtual level */
            uf.union(index, size*size+1);
        }
        else if (colIndex == 0) {
            /* Corners taken care of above */  
            if (percModel[rowIndex][colIndex+1] == 0)
                uf.union(index, index+1);
            if (percModel[rowIndex+1][colIndex] == 0)
                uf.union(index, index+size);
            if (percModel[rowIndex-1][colIndex] == 0)
                uf.union(index, index-size);
        }
        else if (colIndex == size-1) {
            /* Corners taken care of above */
            if (percModel[rowIndex][colIndex-1] == 0)
                uf.union(index, index-1);
            if (percModel[rowIndex+1][colIndex] == 0)
                uf.union(index, index+size);
            if (percModel[rowIndex-1][colIndex] == 0)
                uf.union(index, index-size);
        }
        /* Somewhere in the middle */
        else {
            if (percModel[rowIndex][colIndex-1] == 0)
                uf.union(index, index-1);
            if (percModel[rowIndex][colIndex+1] == 0)
                uf.union(index, index+1);
            if (percModel[rowIndex-1][colIndex] == 0)
                uf.union(index, index-size);
            if (percModel[rowIndex+1][colIndex] == 0)
                uf.union(index, index+size);
        }
    }
    
   // Is site (row, col) open?
    public boolean isOpen(int row, int col) {
        
        if (row <= 0 || row > size) 
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (col <= 0 || col > size) 
            throw new IndexOutOfBoundsException("col index i out of bounds");
        
        int rowIndex = row-1;
        int colIndex = col-1;
        
        return (percModel[rowIndex][colIndex] == 0);
    }
    
    // Is site (row, col) full?
    public boolean isFull(int row, int col) {
        
        if (row <= 0 || row > size) 
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (col <= 0 || col > size) 
            throw new IndexOutOfBoundsException("col index i out of bounds");
        
        /* Get corresponding index in Connection model */
        int index = size*(row - 1) + col;
        
        /* Convert row, col to index in Percolation model */
        int rowIndex = row-1;
        int colIndex = col-1;
        
        /* Check that open site */
        if (percModel[rowIndex][colIndex] == 0) {
            return uf.connected(index, 0);
        }
        else return false;
    }

    // Does the system percolate?
    public boolean percolates() {
        
        /* If 0 element and last element is connected */
        /* the system percolates.                     */
        return uf.connected(0, size*size+1);
        
    }

    // Test client (optional)
    public static void main(String[] args) {


    }
}

