package pers.luo.algs;

import java.util.Iterator;
import java.util.Scanner;

public class SequentialSearchST<Key, Value> implements ST<Key, Value> {
    private int N;
    private final Node head;
    private class Node {
        Key key;
        Value value;
        Node next;
        public Node(Key key, Value value, Node next)
        {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public SequentialSearchST()
    {
        head = new Node(null, null, null);
    }

    @Override
    public void put(Key key, Value value) {
        for (Node cur = head.next; cur != null; cur = cur.next)
            if (cur.key.equals(key)) { cur.value = value; return; }
        head.next = new Node(key, value, head.next);
        N++;
    }

    @Override
    public Value get(Key key) {
        Node prev;
        for (prev = head; prev.next != null; prev = prev.next)
            if (prev.next.key.equals(key)) {
                Node cur = prev.next;
                prev.next = cur.next;
                cur.next = head.next;
                head.next = cur;
                return cur.value;
            }
        return null;
    }

    @Override
    public void delete(Key key) {
        for (Node cur = head; cur.next != null; cur =cur.next)
            if (cur.next.key.equals(key)) { cur.next = cur.next.next; N--; return; }
    }

    @Override
    public int size() {
        return N;
    }

    @Override
    public Iterable<Key> keys() {
        return new ListIterable();
    }
    private class ListIterable implements Iterable<Key> {
        public Iterator<Key> iterator()
        { return new ListIterator(); }
    }
    private class ListIterator implements Iterator<Key> {
        private Node cur = head.next;
        public boolean hasNext()
        { return cur != null; }
        public Key next()
        {
            Key key = cur.key;
            cur = cur.next;
            return key;
        }
    }

    public static void main(String[] args)
    {
        SequentialSearchST<String, Integer> st = new SequentialSearchST<>();
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; scanner.hasNext(); i++) {
            String key = scanner.next();
            st.put(key, i);
        }
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
    }
}
