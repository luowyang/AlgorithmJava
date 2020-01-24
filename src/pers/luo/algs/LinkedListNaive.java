package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListNaive<Item extends Comparable<Item>> implements Iterable<Item> {
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
    private class Nodes {
        Node node1;
        Node node2;
        public Nodes(Node node1, Node node2)
        { this.node1 = node1; this.node2 = node2; }
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

    private Nodes merge(Node lo, Node mid, Node hi)
    {
        Node cur, first;
        if (lo.item.compareTo(mid.next.item) > 0) {
            first = mid.next;
            mid.next = first.next;
            first.next = lo;
        }
        else {
            first = lo;
        }
        cur = first;
        Node left = cur.next;
        while (cur != mid && cur != hi) {
            if (left.item.compareTo(mid.next.item) <= 0) {
                cur = cur.next;
                left = left.next;
            }
            else {
                cur.next = mid.next;
                mid.next = mid.next.next;
                cur.next.next = left;
                cur = cur.next;
            }
        }
        return new Nodes(first, cur==mid?hi:mid);
    }

    public void sort()
    {
        if (isEmpty()) throw new RuntimeException("Cannot sort empty list");
        for (int sz = 1; sz < length; sz += sz)
        {
            Node mid = head;
            Node right;
            for (int count = 1; count < sz; count++) mid = mid.next;
            right = mid.next;
            for (int count = 1; count < sz && right.next != null; count++) right = right.next;
            Nodes nodes = merge(head, mid, right);
            head = nodes.node1;
            Node left;
            while (nodes.node2.next != null) {
                left = nodes.node2;
                mid = left.next;
                for (int count = 1; count < sz && mid.next != null; count++) mid = mid.next;
                right = mid.next;
                if (right == null) break;
                for (int count = 1; count < sz && right.next != null; count++) right = right.next;
                nodes = merge(left.next, mid, right);
                left.next = nodes.node1;
            }
        }
    }

    public boolean isSorted()
    {
        if (isEmpty()) return false;
        for (Node cur = head; cur.next != null; cur = cur.next)
            if (cur.item.compareTo(cur.next.item) > 0) return false;
        return true;
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

    private class LinkedListNaiveIterator implements Iterator<Item> {
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
    { return new LinkedListNaiveIterator(); }

    public static void main(String[] args)
    {
        LinkedListNaive<String> l = new LinkedListNaive<>();
        while(!StdIn.isEmpty()) {
            String s = StdIn.readString();
            // if (!s.equals("-")) l.add(s);
            l.add(s);
        }
        System.out.println("size: " + l.size());
        System.out.println("is sorted: " + l.isSorted());
        l.sort();
        System.out.println("is sorted: " + l.isSorted());
        for (String s : l)
            System.out.print(s + " ");
        System.out.println("");
    }
}