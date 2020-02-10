package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Bag<Item> implements Iterable<Item> {
    private Node<Item> first; // refer to first item
    private int N;
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

    public Bag() {
        first = null;
        N = 0;
    }

    // copy constructor
    public Bag(Bag<Item> bag) {
        if (bag == null) throw new IllegalArgumentException("Null argument for bag copy constructor");
        if (bag.isEmpty()) return;
        this.N = bag.N;
        this.first = new Node<>(bag.first.item, null);
        Node<Item> x = this.first;
        Node<Item> y = bag.first.next;
        while (y != null) {
            x.next = new Node<>(y.item, null);
            x = x.next;
            y = y.next;
        }
    }

    public boolean isEmpty()
    { return first == null; }

    public int size()
    { return N; }

    public void add(Item item)
    {   // add item to the bag
        first = new Node<>(item, first);
        N++;
    }

    public boolean contains(Item item) {
        for (Node<Item> cur = first; cur != null; cur = cur.next)
            if (cur.item.equals(item)) return true;
        return false;
    }

    private class ListIterator implements Iterator<Item> {
        private Node<Item> current = first;

        public boolean hasNext()
        { return current != null; }
        public Item next()
        {
            if(!hasNext()) throw new NoSuchElementException("Bag iterator out of bound");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    public Iterator<Item> iterator()
    { return new ListIterator();}

    public static void main(String[] args)
    {
        Bag<String> bag = new Bag<>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            bag.add(item);
        }
        System.out.print("remnants on bag:");
        for (String s : bag)
            System.out.print(" " + s);
    }
}