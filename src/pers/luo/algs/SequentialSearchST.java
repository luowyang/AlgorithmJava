package pers.luo.algs;

import java.util.Scanner;

public class SequentialSearchST<Key, Value> implements ST<Key, Value> {
    private int N;
    private Node head;
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
    }

    @Override
    public Value get(Key key) {
        for (Node cur = head.next; cur != null; cur = cur.next)
            if (cur.key.equals(key)) return cur.value;
        return null;
    }

    @Override
    public int size() {
        return N;
    }

    @Override
    public Iterable<Key> keys() {
        return null;
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
