package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

// linked list with sentinel
public class LinkedList<Item extends Comparable<Item>> implements Iterable<Item> {
    private int length;
    private Node head = new Node(null, null);   // sentinel node
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
        head.next = new Node(item, head.next);
        length++;
    }

    public void delete(int index)
    {
        if (index <= 0 || index > length)
            throw new NoSuchElementException("Linked List index out of bound");
        /*if (index == 1) {
            head = head.next;
        }
        else {
            Node prev = head;
            for (int k = 1; k < index - 1; k++)
                prev = prev.next;
            removeAfter(prev);
        }*/
        Node prev = head;
        for (int k = 1; k < index - 1; k++)
            prev = prev.next;
        removeAfter(prev);
        length--;
    }

    public boolean find(Item key)
    {
        Node cur = head.next;
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

    private Node merge(Node left, Node right)
    {
        Node first = new Node(null, left);
        Node cur = first;
        while (left != null && right != null)
        {
            if (left.item.compareTo(right.item) <= 0) {
                cur.next = left;
                left = left.next;
            }
            else {
                cur.next = right;
                right = right.next;
            }
            cur = cur.next;
        }
        if (left == null) cur.next = right;
        else              cur.next = left;
        return first.next;
    }

    public void sort()
    {
        head.next = sortTD(head.next);
    }

    private Node sortTD(Node node)
    {
        if (node.next == null) return node;
        Node right = node;
        Node fast = node.next;   //fast and slow pointers
        while (fast != null && fast.next != null) {
            right = right.next;
            fast = fast.next.next;
        }
        Node t = right.next;
        right.next = null;
        right = t;
        node = sortTD(node);
        right = sortTD(right);
        return merge(node, right);
    }

    public void sortBU()
    {
        if (isEmpty()) throw new RuntimeException("Cannot sort empty list");
        for (int sz = 1; sz < length; sz += sz)
        {
            Node left = head;
            Node right = head.next;
            while (right != null) {
                for (int count = 1; count < sz && right.next != null; count++) right = right.next;
                if (right.next == null) break;
                Node t = right.next;
                right.next = null;
                right = t;
                for (int count = 1; count < sz && t.next != null; count++) t = t.next;
                if (t.next == null) {
                    left.next = merge(left.next, right);
                    break;
                }
                Node t2 = t.next;
                t.next = null;
                t = t2;
                left.next = merge(left.next, right);
                while (left.next != null) left = left.next;
                left.next = t;
                right = t;
            }
        }
    }

    public void natural()
    {
        Node left = head;
        Node right = head.next;
        Node end = head.next;
        while (left != head || (right.next != null && end.next != null)) {
            left = head;
            right = head.next;
            end = head.next;
            while (true) {
                // find left sorted sublist and cut it off
                while (right.next != null && right.item.compareTo(right.next.item) <= 0)
                    right = right.next;
                if (right.next == null) break;  // if no right sublist finish this iteration
                end = right.next;
                right.next = null;
                right = end;
                // find right sorted sublist and cut it off for merge()
                while (end.next != null && end.item.compareTo(end.next.item) <= 0)
                    end = end.next;
                if (end.next == null) {
                    left.next = merge(left.next, right);
                    break;
                }
                Node t = end.next;
                end.next = null;
                end = t;
                left.next = merge(left.next, right);
                // set to next state
                while (left.next != null) left = left.next;
                left.next = end;
                right = end;
            }
        }
    }

    public boolean isSorted()
    {
        if (isEmpty()) return false;
        for (Node cur = head.next; cur.next != null; cur = cur.next)
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
        head = head.next;
        Node first = head.next;
        head.next = null;
        while (first != null) {
            Node second = first.next;
            first.next = head;
            head = first;
            first = second;
        }
        head = new Node(null, head);
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
    { head = new Node(null, reverseRecursive(head.next)); }

    private class LinkedListIterator implements Iterator<Item> {
        private Node cur = head.next;
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
            // if (!s.equals("-")) l.add(s);
            l.add(s);
        }
        System.out.println("size: " + l.size());
        System.out.println("is sorted: " + l.isSorted());
        for (String s : l)
            System.out.print(s + " ");
        System.out.println("");
//        l.sort();
//        l.sortBU();
        l.natural();
        System.out.println("is sorted: " + l.isSorted());
        for (String s : l)
            System.out.print(s + " ");
        System.out.println("");
    }
}
