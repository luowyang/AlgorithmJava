package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private DoubleNode<Item> left;
    private DoubleNode<Item> right;
    private int N;
    private static class DoubleNode<Item> {
        Item item;
        DoubleNode<Item> prev;
        DoubleNode<Item> next;
        public DoubleNode(Item item, DoubleNode<Item> prev, DoubleNode<Item> next)
        {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    public boolean isEmpty()
    { return left == null; }

    public int size()
    { return N; }

    public void pushLeft(Item item)
    {
        left = new DoubleNode<Item>(item, null, left);
        if (left.next == null) right = left;
        else                   left.next.prev = left;
        N++;
    }

    public void pushRight(Item item)
    {
        right = new DoubleNode<Item>(item, right, null);
        if (right.prev == null) left = right;
        else                    right.prev.next = right;
        N++;
    }

    public Item popLeft()
    {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = left.item;
        left = left.next;
        if (left == null) right = null;
        else              left.prev = null;
        N--;
        return item;
    }

    public Item popRight()
    {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = right.item;
        right = right.prev;
        if (right == null) left = null;
        else               right.next = null;
        N--;
        return item;
    }

    private class DoublyLinkedListIterator implements Iterator<Item> {
        private DoubleNode<Item> current= left;  // current indicates which element to be took

        public boolean hasNext()
        { return current != null; }
        public Item next()
        {
            if(!hasNext()) throw new NoSuchElementException("Steque iterator out of bound");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    public Iterator<Item> iterator()
    {
        return new DoublyLinkedListIterator();
    }

    public static void main(String[] args)
    {
        Deque<String> deque = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (item.equals("-"))
                deque.pushLeft(item);
            else
                deque.pushRight(item);
        }
        System.out.println("(" + deque.size() + " left on deque)");
        System.out.print("remnants on deque:");
        for (String s : deque)
            System.out.print(" " + s);
        System.out.println("");
        while (deque.popLeft().equals("-"));
        System.out.print("remnants on deque:");
        for (String s : deque)
            System.out.print(" " + s);
        System.out.println("");
        while (!deque.isEmpty())
            System.out.print(deque.popRight() + " ");
        System.out.println("");
    }
}
