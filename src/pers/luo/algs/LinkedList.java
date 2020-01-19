package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<Item extends Comparable<Item>> implements Iterable<Item> {
    private int length;
    private Node head;
    private class Node {
        Item item;
        Node next;
        public Node(Item item, Node next)
        {
            this.item = item;
            this.next = next;
        }
    }

    public boolean isEmpty()
    { return length == 0; }

    public int size()
    { return length; }

    public void add(Item item)
    {
        head = new Node(item, head);
        length++;
    }

    public void delete(int index)
    {
        if (index <= 0 || index > length)
            throw new NoSuchElementException("Linked List index out of bound");
        if (index == 1) {
            head = head.next;
        }
        else {
            Node prev = head;
            for (int k = 1; k < index - 1; k++)
                prev = prev.next;
            removeAfter(prev);
        }
        length--;
    }

    public boolean find(Item key)
    {
        Node cur = head;
        while (cur != null && !cur.item.equals(key)) cur = cur.next;
        return cur != null;
    }

    private void removeAfter(Node node)
    {
        if (node == null || node.next == null) return;
        node.next = node.next.next;
    }

    private void insertAfter(Node prev, Node node)
    {
        if (prev == null || node == null) return;
        node.next = prev.next;
        prev.next = node;
    }

    public void remove(Item key)
    {
        while (head != null && head.item.equals(key)) {
            head = head.next;
            length--;
        }
        if (isEmpty()) return;
        for (Node prev = head; prev.next != null;) {
            if (prev.next.item.equals(key)) {
                removeAfter(prev);
                length--;
            }
            else {
                prev = prev.next;
            }
        }
    }

    public Item max(Node first)
    {
        if (first == null) throw new NoSuchElementException("Max value does not exist");
        if (first.next == null) return first.item;
        Item tmp = max(first.next);
        if (first.item.compareTo(tmp) > 0) return first.item;
        else                               return tmp;
    }

    public void reverseLoop()
    {
        if (isEmpty()) return;
        Node first = head.next;
        head.next = null;
        while (first != null) {
            Node second = first.next;
            first.next = head;
            head = first;
            first = second;
        }
    }

    public Node reverseRecursive(Node node)
    {
        if (node == null) return null;
        if (node.next == null) return node;
        Node first = node.next;
        Node rev = reverseRecursive(first);
        first.next = node;
        node.next = null;
        return rev;
    }

    public void reverse()
    { head = reverseRecursive(head); }

    private class LinkedListIterator implements Iterator<Item> {
        private Node cur = head;
        public boolean hasNext()
        { return cur != null;}
        public Item next()
        {
            Item item = cur.item;
            cur = cur.next;
            return item;
        }
    }
    public Iterator<Item> iterator()
    { return new LinkedListIterator(); }

    public static void main(String[] args)
    {
        LinkedList<String> l = new LinkedList<>();
        while(!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (!s.equals("-")) l.add(s);
        }
        System.out.println("size: " + l.size());
        // delete test
        l.delete(l.size());
        System.out.println("size: " + l.size());
        // find test
        System.out.println("find that: " + l.find("that"));
        System.out.println("find ha: " + l.find("ha"));
        // print linked list
        for (String ss : l)
            System.out.print(ss + " ");
        System.out.println("");
        // remove test
        l.remove("be");
        for (String ss : l)
            System.out.print(ss + " ");
        System.out.println("");
        System.out.println("Max value: " + l.max(l.head));
        // reverse by loop
        l.reverseLoop();
        System.out.print("Reverse by loop: ");
        for (String ss : l)
            System.out.print(ss + " ");
        System.out.println("");
        // reverse by recursion
        l.reverse();
        System.out.print("Reverse by recursion: ");
        for (String ss : l)
            System.out.print(ss + " ");
        System.out.println("");
    }
}
