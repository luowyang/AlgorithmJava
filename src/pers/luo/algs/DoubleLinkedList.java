package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

public class DoubleLinkedList<Item> implements Iterable<Item> {
    private DoubleNode first;
    private DoubleNode last;
    private int N;
    private class DoubleNode {
        Item item;
        DoubleNode prev;
        DoubleNode next;
        public DoubleNode(Item item, DoubleNode prev, DoubleNode next)
        {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    public boolean isEmpty()
    { return first == null; }

    public int size()
    { return N; }

    public void insertFirst(Item item)
    {
        first = new DoubleNode(item, null, first);
        if (first.next == null) last = first;
        else                    first.next.prev = first;
        N++;
    }

    public void insertLast(Item item)
    {
        last = new DoubleNode(item, last, null);
        if (last.prev == null) first = last;
        else                   last.prev.next = last;
        N++;
    }

    public void deleteFirst()
    {
        if (isEmpty()) throw new RuntimeException("Double linked list underflow");
        first = first.next;
        if (first == null) last = null;
        else               first.prev = null;
        N--;
    }

    public void deleteLast()
    {
        if (isEmpty()) throw new RuntimeException("Double linked list underflow");
        last = last.prev;
        if (last == null) first = null;
        else              last.next = null;
        N--;
    }

    public void insertBefore(DoubleNode node, Item item)
    {
        if (node == null || item == null) return;
        node.prev = new DoubleNode(item, node.prev, node);
        if (node.prev.prev != null) node.prev.prev.next = node.prev;
        else                        first = node.prev;
        N++;
    }

    public void insertAfter(DoubleNode node, Item item)
    {
        if (node == null || item == null) return;
        node.next = new DoubleNode(item, node, node.next);
        if (node.next.next != null) node.next.next.prev = node.next;
        else                        last = node.next;
        N++;
    }

    public void delete(DoubleNode node)
    {
        if (node == null) return;
        if (node.prev != null) node.prev.next = node.next;
        else                   first = node.next;
        if (node.next != null) node.next.prev = node.prev;
        else                   last = node.prev;
        N--;
    }

    private class DoubleLinkedListIterator implements Iterator<Item> {
        private DoubleNode cur = first;
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
    { return new DoubleLinkedListIterator(); }

    public static void main(String[] args)
    {
        DoubleLinkedList<String> l = new DoubleLinkedList<>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (s.equals("-")) l.insertFirst(s);
            else               l.insertLast(s);
        }
        printList(l, "Items on double linked list:");
        // delete first and last test
        l.deleteFirst();
        printList(l, "delete first:");
        l.deleteLast();
        printList(l, "delete last:");
        // insert before and after test
        l.insertAfter(l.first, "after");
        printList(l, "insert after first:");
        l.insertBefore(l.first, "before");
        printList(l, "insert before first:");
        // delete test
        l.delete(l.first);l.delete(l.first);
        l.delete(l.last);l.delete(l.last);
        printList(l, "delete test:");
    }

    public static void printList(DoubleLinkedList<String> list, String message)
    {
        System.out.print(message);
        for (String s : list) System.out.print(" " + s);
        System.out.println("");
    }
}
