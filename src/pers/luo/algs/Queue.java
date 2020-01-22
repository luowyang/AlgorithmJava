package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue<Item> implements Iterable<Item> {
    private Node<Item> first; // refer to first item
    private Node<Item> last;  // refer to last item
    private int N;      // size of queue
    private static class Node<Item>
    {   // private static nested Node class for linked list, only accessed by enclosing class
        // should be declared a generic because static nested class cannot access parameterized type if Outer class
        // "Item" of Node has different meaning than that of the top-level class
        Item item;
        Node<Item> next;

        public Node(Item item, Node<Item> next)
        {
            this.item = item;
            this.next = next;
        }
    }

    public Queue() { }

    public Queue(Queue<Item> source)
    {
        for (Item item : source)
            this.enqueue(item);
    }

    public boolean isEmpty()
    { return first == null; }

    public int size()
    { return N; }

    public void enqueue(Item item)
    {   // add item to the end
        if (isEmpty()) {
            last = new Node<Item>(item, null);
            first = last;
        }
        else {
            last.next = new Node<Item>(item, null);
            last = last.next;
        }
        N++;
    }

    public Item dequeue()
    {   // delete and return item from beginning
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item = first.item;
        first = first.next;
        if (isEmpty()) last = null;
        N--;
        return item;
    }

    private class ListIterator implements Iterator<Item> {
        private Node<Item> current = first;

        public boolean hasNext()
        { return current != null; }
        public Item next()
        {
            if(!hasNext()) throw new NoSuchElementException("Queue iterator out of bound");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    public Iterator<Item> iterator()
    { return new ListIterator();}

    public static void main(String[] args)
    {
        Queue<String> queue = new Queue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                queue.enqueue(item);
            else if (!queue.isEmpty())
                System.out.print(queue.dequeue() + " ");
        }
        System.out.println("(" + queue.size() + " left on queue)");
        Queue<String> another = new Queue<>(queue);
        System.out.print("remnants on another queue:");
        for (String s : another)
            System.out.print(" " + s);
        System.out.println("");
        System.out.print("remnants on queue:");
        for (String s : queue)
            System.out.print(" " + s);
        System.out.println("");
    }
}
