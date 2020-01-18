package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

public class Queue<Item> implements Iterable<Item> {
    private Node first; // refer to first item
    private Node last;  // refer to last item
    private int N;      // size of queue
    private class Node
    {   // embedded class for Node of linked list
        Item item;
        Node next;

        public Node(Item item, Node next)
        {
            this.item = item;
            this.next = next;
        }
    }

    public boolean isEmpty()
    { return first == null; }

    public int size()
    { return N; }

    public void enqueue(Item item)
    {   // add item to the end
        if (isEmpty()) {
            last = new Node(item, null);
            first = last;
        }
        else {
            last.next = new Node(item, null);
            last = last.next;
        }
        N++;
    }

    public Item dequeue()
    {   // delete and return item from beginning
        Item item = first.item;
        first = first.next;
        if (isEmpty()) last = null;
        N--;
        return item;
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext()
        { return current != null; }
        public Item next()
        {
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
        System.out.print("remnants on queue:");
        for (String s : queue)
            System.out.print(" " + s);
    }
}
