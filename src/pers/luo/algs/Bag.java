package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

public class Bag<Item> implements Iterable<Item> {
    private Node first; // refer to first item
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

    public void add(Item item)
    {   // add item to the bag
        first = new Node(item, first);
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
        Bag<String> bag = new Bag<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            bag.add(item);
        }
        System.out.print("remnants on bag:");
        for (String s : bag)
            System.out.print(" " + s);
    }
}