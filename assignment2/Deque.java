/* Implements a 'Deck'. Uses linked-list implementation */

import java.lang.*;
import java.util.*;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
    

public class Deque<Item> implements Iterable<Item> {
    
    private Node first;
    private Node last;
    private int N;
    
    private class Node {
        Item item = null;
        Node next = null;
        Node prev = null;
    }
   
    // construct an empty deque
    public Deque() {
        /* Init privates */
        first = null;
        last = null;
        N = 0;
    }
    
    // is the deque empty?
    public boolean isEmpty() {
        return (first == null);
    }

    // return the number of items on the deque
    public int size() {
        return N;
    }
    
    // add the item to the front
    public void addFirst(Item item) {
        /* Check if in-argument is null */
        if (item == null)
            throw new NullPointerException("item = null");
        
        /* Check if 'Dequeue' is empty */
        if (isEmpty()) {
            /* Create a new node */
            first = new Node();
            first.item = item;
            last = first;
        }
        else {
            Node oldfirst = first; // save the old first
            /* Create a new node */
            first = new Node();
            first.item = item;     // set first to new item
            first.next = oldfirst; // let new first point to the old first
            oldfirst.prev = first; // let oldfirst have pointer back to prev
        }
        /* Increase size */
        N++;
    }
    
    // add the item to the end
    public void addLast(Item item) {
        /* Check if in-argument is null */
        if (item == null)
            throw new NullPointerException("item = null");
        
        /* Check if 'Dequeue' is empty */
        if (isEmpty()) {
            last = new Node();
            last.item = item;
            first = last;
        }
        else {
            Node oldlast = last; // save current last
            last = new Node();   // create new node
            last.item = item;    // set last to new item
            oldlast.next = last; // set the old last to point at new last
            last.prev = oldlast; // let the new last point back to oldlast
        }
        
        /* Increase size */
        N++;
    }
    
    // remove and return the item from the front
    public Item removeFirst() {
        /* No queue exist */
        if (first == null)
            throw new NoSuchElementException("no elements");
        
        Item item = first.item;     // item to return
        if (first.next == null) {   // if it was the last element
            first = null;
            last = null;
        }
        else {
            first = first.next;     // increase first to second item
            first.prev = null;      // set prev pointer to null
        }
        N--;
        return item;
    }
    
    // remove and return the item from the end
    public Item removeLast() {
        /* No queue exist */
        if (last == null)
            throw new NoSuchElementException("no elements");
        
        Item item = last.item; // item to return
        if(last.prev != null) {
            last = last.prev;
            last.next = null;
        }
        else {
            last = null;
            first = last;
        }
        N--;
        return item;
    }
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator()  {
        return new DequeIterator();  
    }

    // an iterator, doesn't implement remove() since it's optional
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
    
    // unit testing
    public static void main(String[] args) {
        
        Deque<String> q = new Deque<String>();
        
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            q.addLast(item);
            q.addFirst(item);
        }
        
        while(!q.isEmpty()) {
            StdOut.print(q.removeLast() + " ");
            StdOut.print(q.removeFirst() + " ");
        } 
    }
}
