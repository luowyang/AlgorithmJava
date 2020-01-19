package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Steque<Item> implements Iterable<Item> {
    private Node<Item> first;
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
    { return first == null; }

    public int size()
    { return N; }

    public void push(Item item)
    {
        first = new Node<Item>(item, first);
        N++;
    }

    public Item pop()
    {
        if (isEmpty()) throw new NoSuchElementException("Steque underflow");
        Item item = first.item;
        first = first.next;
        N--;
        return item;
    }

    public void enqueue(Item item)
    {
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

    private class ListIterator implements Iterator<Item> {
        private Node<Item> current= first;  // current indicates which element to be took

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
        return new ListIterator();
    }

    public static void main(String[] args)
    {
        Steque<String> steque = new Steque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                steque.enqueue(item);
            else if (!steque.isEmpty())
                System.out.print(steque.pop() + " ");
        }
        System.out.println("(" + steque.size() + " left on steque)");
        System.out.print("remnants on steque:");
        for (String s : steque)
            System.out.print(" " + s);
    }
}
