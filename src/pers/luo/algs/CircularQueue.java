package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CircularQueue<Item> implements Iterable<Item> {
    private Node<Item> last;
    private int N;
    private static class Node<Item> {
        Item item;
        Node<Item> next;
        public Node(Item item, Node<Item> next)
        {
            this.item = item;
            this.next = next;
        }
    }

    public boolean isEmpty()
    { return last == null; }

    public int size()
    { return N; }

    public void enqueue(Item item)
    {
        if (isEmpty()) {
            last = new Node<Item>(item, null);
            last.next = last;   // maintain circular
        }
        else {
            last.next = new Node<Item>(item, last.next);
            last = last.next;
        }
        N++;
    }

    public Item dequeue()
    {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item = last.next.item;
        if (N == 1) last = null;
        else        last.next = last.next.next;
        N--;
        return item;
    }

    private class CircularLinkedListIterator implements Iterator<Item> {
        private Node<Item> cur = last;
        private int count = 0;
        public boolean hasNext() {
            return count < N;
        }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("Queue iterator out of bound");
            Item item = cur.next.item;
            cur = cur.next;
            count++;
            return item;
        }
    }
    public Iterator<Item> iterator()
    { return new CircularLinkedListIterator(); }

    public static void main(String[] args)
    {
        CircularQueue<String> queue = new CircularQueue<String>();
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
