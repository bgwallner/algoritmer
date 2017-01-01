import edu.princeton.cs.algs4.Queue;
public class Board {
    
    private int[][] board;
    private int boardSize;
    private int blankRow;
    private int blankCol;
    
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        
        boardSize = blocks.length;
        /* Create matrix */
        board = new int[boardSize][boardSize];
        for (int i=0; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                /* Copy array to local */
                board[i][j] = blocks[i][j];
                /* Keep track of blank */
                if (blocks[i][j] == 0) {
                    /* Separate index from (row,col) -> +1 */
                    blankRow = i+1;
                    blankCol = j+1;
                }
            }
        }
    }
        
    // board dimension n
    public int dimension() {
        return boardSize;
    }
        
    // number of blocks out of place
    public int hamming() {
        int total = 0;
        for (int i=0; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                /* don't count blank */
                if (board[i][j] != 0) {
                    int goalRowIndex = (board[i][j] - 1) / boardSize;
                    int goalColIndex = (board[i][j] - 1) % boardSize;
                    if (goalRowIndex != i || goalColIndex != j)
                        total++; 
                }
            }
        }
        return total;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int total = 0;
        for (int i=0; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                /* don't count blank */
                if (board[i][j] != 0) {
                    int goalRowIndex = (board[i][j] - 1) / boardSize;
                    int goalColIndex = (board[i][j] - 1) % boardSize;
                    total+=Math.abs(goalRowIndex - i) + Math.abs(goalColIndex - j);
                }
            }
        }
        return total;
    }
    
    // is this board the goal board?
    public boolean isGoal() {
        /* If hamming distances = 0 it's the goal board */
        return (this.hamming() == 0);
    }
        
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        
        /* Choose exchangeRow */
        int exchangeRow;
        if (blankRow == 1) 
            exchangeRow = (blankRow + 1);
        else
            exchangeRow = blankRow - 1;
        
        /* choose exchangeCol */
        int exchangeCol1 = blankCol;
        int exchangeCol2;
        if (blankCol == 1)
            exchangeCol2 = blankCol + 1;
        else
            exchangeCol2 = blankCol - 1;
        
        /* Copy board to twinBoard */
        int[][] twinBoard = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++)
                twinBoard[i][j] = board[i][j];
        
        /* Using exchangeRow guarentess that no blank will exist on that row  */
        int temp = twinBoard[exchangeRow-1][exchangeCol1-1];
        twinBoard[exchangeRow-1][exchangeCol1-1] = twinBoard[exchangeRow-1][exchangeCol2-1];
        twinBoard[exchangeRow-1][exchangeCol2-1] = temp;
        
        /* return a new twinboard */
        return new Board(twinBoard);
    }
        
    // does this board equal y?
    public boolean equals(Object y) {
        /* Use some standard compare */
        if (this==y)                         
            return true;
        if (this == null)                    
            return false;
        if (this.getClass() != y.getClass()) 
            return false;
         
        /* Check if content the same */
        Board equalBoard = (Board) y;
        if (equalBoard.boardSize == this.boardSize) {
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (equalBoard.board[i][j] != this.board[i][j])
                        return false;
                }
            }
        }
        else {
            /* No matching length */
            return false;
        }
        return true;
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();
        Board duplicate; // Board pointer
        
        /* Now check if OK to move in any of 4 directions */
        
        /* Check if OK to "move blank up" */
        if (blankRow > 1) {
            duplicate = duplicate();
            /* Exchange blank and update blankRow/Col */
            duplicate.board[blankRow-1][blankCol-1] = duplicate.board[blankRow-2][blankCol-1];
            duplicate.board[blankRow-2][blankCol-1] = 0;
            duplicate.blankRow--;
            q.enqueue(duplicate);
        }
        
        /* Check if OK to "move blank down" */
        if (blankRow < boardSize) {
            duplicate = duplicate();
            /* Exchange blank and update blankRow/Col */
            duplicate.board[blankRow-1][blankCol-1] = duplicate.board[blankRow][blankCol-1];
            duplicate.board[blankRow][blankCol-1] = 0;
            duplicate.blankRow++;
            q.enqueue(duplicate);
        }
        
        /* Check if OK to "move blank left" */
        if (blankCol > 1) {
            duplicate = duplicate();
            /* Exchange blank and update blankRow/Col */
            duplicate.board[blankRow-1][blankCol-1] = duplicate.board[blankRow-1][blankCol-2];
            duplicate.board[blankRow-1][blankCol-2] = 0;
            duplicate.blankCol--;
            q.enqueue(duplicate);
        }
        
        /* Check if OK to "move right */
        if (blankCol < boardSize) {
            duplicate = duplicate();
            /* Exchange blank and update blankRow/Col */
            duplicate.board[blankRow-1][blankCol-1] = duplicate.board[blankRow-1][blankCol];
            duplicate.board[blankRow-1][blankCol] = 0;
            duplicate.blankCol++;
            q.enqueue(duplicate); 
        }
        
        return q;
    }
    
    private Board duplicate() {
        /* Create a duplicate board */
        int[][] duplicate = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++)
                duplicate[i][j] = board[i][j];
        
        return new Board(duplicate);
    }
        
   // string representation of this board (in the output format specified below)
    public String toString()  {
        StringBuilder str = new StringBuilder();
        str.append(boardSize + "\n");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                str.append(String.format("%2d ", board[i][j]));
            }
            str.append("\n");
        }
        return str.toString();
    }
    
    // unit tests (not graded)
    public static void main(String[] args) {
        
    }
}