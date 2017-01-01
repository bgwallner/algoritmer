import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {
    public static void main(String[] args) {
        /* Get input */
        String s = StdIn.readString();
        int k = Integer.valueOf(args[0]);
        
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        q.enqueue(s);
        
        while (!StdIn.isEmpty()) {
            s = StdIn.readString();
            q.enqueue(s);
        }
        
        for (int i = 0; i < k; i++) {
            System.out.println(q.dequeue());
        }
    }
}