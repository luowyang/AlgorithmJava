package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

public class DoublyLinkedList<Item> implements Iterable<Item> {
    private DoubleNode sent;
    private int N;
    private class DoubleNode {
        Item item;
        DoubleNode prev;
        DoubleNode next;
        public DoubleNode() { }
        public DoubleNode(Item item, DoubleNode prev, DoubleNode next)
        {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    public DoublyLinkedList()
    {
        sent = new DoubleNode();
        sent.prev = sent;
        sent.next = sent;
    }

    public boolean isEmpty()
    { return N == 0; }

    public int size()
    { return N; }

    public void insertFirst(Item item)
    {
        sent.next = new DoubleNode(item, sent, sent.next);
        sent.next.next.prev = sent.next;
        N++;
    }

    public void insertLast(Item item)
    {
        sent.prev = new DoubleNode(item, sent.prev, sent);
        sent.prev.prev.next = sent.prev;
        N++;
    }

    public void deleteFirst()
    {
        if (isEmpty()) throw new RuntimeException("Double linked list underflow");
        sent.next = sent.next.next;
        sent.next.prev = sent;
        N--;
    }

    public void deleteLast()
    {
        if (isEmpty()) throw new RuntimeException("Double linked list underflow");
        sent.prev = sent.prev.prev;
        sent.prev.next = sent;
        N--;
    }

    public void insertBefore(DoubleNode node, Item item)
    {
        if (node == null || item == null) return;
        node.prev = new DoubleNode(item, node.prev, node);
        node.prev.prev.next = node.prev;
        N++;
    }

    public void insertAfter(DoubleNode node, Item item)
    {
        if (node == null || item == null) return;
        node.next = new DoubleNode(item, node, node.next);
        node.next.next.prev = node.next;
        N++;
    }

    public void delete(DoubleNode node)
    {
        if (node == null) return;
        node.prev.next = node.next;
        node.next.prev = node.prev;
        N--;
    }

    private class DoublyLinkedListIterator implements Iterator<Item> {
        private DoubleNode cur = sent.next;
        public boolean hasNext()
        { return cur != sent;}
        public Item next()
        {
            Item item = cur.item;
            cur = cur.next;
            return item;
        }
    }
    public Iterator<Item> iterator()
    { return new DoublyLinkedListIterator(); }

    public static void main(String[] args)
    {
        DoublyLinkedList<String> l = new DoublyLinkedList<>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (s.equals("-")) l.insertFirst(s);
            else               l.insertLast(s);
        }
        printList(l, "Items on doubly linked list:");
        // delete first and last test
        l.deleteFirst();
        printList(l, "delete first:");
        l.deleteLast();
        printList(l, "delete last:");
        // insert before and after test
        l.insertAfter(l.sent.next, "after");
        printList(l, "insert after first:");
        l.insertBefore(l.sent.next, "before");
        printList(l, "insert before first:");
        // delete test
        l.delete(l.sent.next);l.delete(l.sent.next);
        l.delete(l.sent.prev);l.delete(l.sent.prev);
        printList(l, "delete test:");
    }

    public static void printList(DoublyLinkedList<String> list, String message)
    {
        System.out.print(message);
        for (String s : list) System.out.print(" " + s);
        System.out.println("");
    }
}
