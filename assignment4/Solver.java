import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
//import java.util.*;

public class Solver {

    /* Two instances of Priority queues  */
    private MinPQ<SearchNode> initPQ;
    private MinPQ<SearchNode> twinPQ;
    /* Whether the puzzle is solvable */
    private boolean isSolvable;
    /* The final board to be found */
    private SearchNode finalBoard;


    /* Define a searchnode, search node of the game to be a board    */
    /* the number of moves made to reach the board, and the previous */
    /* search node                                                   */
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode previous;
        private int nbrOfMoves;
        
        /* Define the Comparable compareTo() using Manhattan prio */
        public int compareTo(SearchNode node) {
            int a = this.board.manhattan() + this.nbrOfMoves;
            int b = node.board.manhattan() + node.nbrOfMoves;
            return a - b;
        }
        
        public SearchNode(Board boardNode, int moves, SearchNode searchNode) {
            board = boardNode;
            nbrOfMoves = moves;
            previous = searchNode;
        }
    }

    /* Define constructor */
    public Solver(Board initial) {
                
        if (initial == null)
            throw new NullPointerException("initial = null");
        
        /* Add initial board, 0 moves, and a null previous search node */
        initPQ = new MinPQ<SearchNode>();
        initPQ.insert(new SearchNode(initial, 0, null));
        
        /* Do the same for the twin */
        twinPQ = new MinPQ<SearchNode>();
        twinPQ.insert(new SearchNode(initial.twin(), 0, null));
        
        /* Add two search node pointers */
        SearchNode init;
        SearchNode twin;
        while (true) {
            
            /* delete from the priority queue the search node with */
            /* the minimum priority                                */
            init = initPQ.delMin();
            twin = twinPQ.delMin();

            /* Check if we reached goal, then bail out of while */
            if (init.board.isGoal()) {
                finalBoard = init;
                isSolvable = true;
                break;
            }
            
            /* Check if twin also reached goal */
            if (twin.board.isGoal()) {
                finalBoard = twin;
                isSolvable = false;
                break;
            }
            
            /* insert onto the priority queue all neighboring search nodes */
            for (Board initBoard : init.board.neighbors()) {
                /* don't enqueue a neighbor if its board is the same as the */
                /* board of the previous search node                        */
                if (init.previous == null || !initBoard.equals(init.previous.board))
                    initPQ.insert(new SearchNode(initBoard, init.nbrOfMoves + 1, 
                    init));
            }
            /* Do the same for twin */
            for (Board twinBoard : twin.board.neighbors()) {
                if (twin.previous == null || !twinBoard.equals(twin.previous.board))
                    twinPQ.insert(new SearchNode(twinBoard, twin.nbrOfMoves + 1, 
                    twin));
            }
        }
    }

    /* Returns whether or not the input board was solvable. */
    public boolean isSolvable() {
        return isSolvable;
    }

    /* Returns the number of nbrOfMoves to solve the input board or -1 if the board wasn't
     * solvable. */
    public int moves() {
        if (this.isSolvable)
            return finalBoard.nbrOfMoves;
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (this.isSolvable) {
            
            Queue<Board> queue = new Queue<Board>();
            SearchNode current = finalBoard;
            while (current != null) {
                queue.enqueue(current.board);
                current = current.previous;
            }
            return queue;
        }
        return null;
    }


    public static void main(String[] args) {

    }
}