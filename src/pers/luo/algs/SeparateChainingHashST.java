package pers.luo.algs;

import java.util.Iterator;
import java.util.Scanner;

@SuppressWarnings("unchecked")
public class SeparateChainingHashST<Key, Value> implements ST<Key, Value> {
    private Node<Key, Value>[] st;  // array of lists
    private int N;      // # of key-value pairs
    private int M;      // # of lists

    private static class Node<Key, Value> {
        Key key;
        Value value;
        Node<Key, Value> next;
        public Node(Key key, Value value, Node<Key, Value> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(Key key) { // must mask sign bit so as not to get negative hash
        return (key.hashCode() & 0x7fffffff) % M;
    }

    public SeparateChainingHashST(int M) {
        this.M = M;
        st = (Node<Key, Value>[]) new Node[M];
    }

    public SeparateChainingHashST() {
        this(9997);
    }

    @Override
    public void put(Key key, Value value) {
        int hash = hash(key);
        Node<Key, Value> cur = st[hash];
        while (cur != null && !cur.key.equals(key)) cur = cur.next;
        if (cur != null) { cur.value = value; return; }
        st[hash] = new Node<Key, Value>(key, value, st[hash]);
        N++;
    }

    @Override
    public Value get(Key key) {
        Node<Key, Value> cur = st[hash(key)];
        while (cur != null && !cur.key.equals(key)) cur = cur.next;
        return cur == null ? null : cur.value;
    }

    @Override
    public int size() {
        return N;
    }

    @Override
    public void delete(Key key) {
        int hash = hash(key);
        if (st[hash] == null) return;
        if (st[hash].key.equals(key)) {
            st[hash] = st[hash].next;
            N--;
            return;
        }
        Node<Key, Value> prev = st[hash];
        while (prev.next != null && !prev.next.key.equals(key)) prev = prev.next;
        if (prev.next != null) {
            prev.next = prev.next.next;
            N--;
        }
    }

    @Override
    public Iterable<Key> keys() {
        return new SeparateChainingIterable();
    }
    private class SeparateChainingIterable implements Iterable<Key> {
        public Iterator<Key> iterator() {
            return new SeparateChainingIterator();
        }
    }
    private class SeparateChainingIterator implements Iterator<Key> {
        private int i = 0, count = 0;
        private Node<Key, Value> cur = st[0];
        public SeparateChainingIterator() {
            setCur();
        }
        public boolean hasNext() {
            return count < N;
        }
        public Key next() {
            Key key = cur.key;
            cur = cur.next;
            count++;
            if (count < N) setCur();
            return key;
        }
        private void setCur() { // set cur to next non-empty slot if cur is null
            if (cur == null) {
                while (st[++i] == null && i < M-1);    // find next non-empty st[i]
                cur = st[i];    // set cur to point to st[i]'s first node
            }
        }
    }

    public static void main(String[] args)
    {
        SeparateChainingHashST<String, Integer> st = new SeparateChainingHashST<>();
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; scanner.hasNext(); i++) {
            String key = scanner.next();
            st.put(key, i);
        }
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
        st.delete("E");
        st.delete("R");
        st.delete("L");
        System.out.println("Deletion test:");
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
    }
}
