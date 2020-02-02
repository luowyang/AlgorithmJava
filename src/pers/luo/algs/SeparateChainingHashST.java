package pers.luo.algs;

import java.util.Iterator;
import java.util.Scanner;

@SuppressWarnings("unchecked")
public class SeparateChainingHashST<Key, Value> implements ST<Key, Value> {
    private Node<Key, Value>[] st;  // array of lists
    private int size;                  // # of key-value pairs
    private int M;                  // # of lists
    private int alpha;              // acceptable average probing numbers
    private int logM;

    private final int[] primes = {
            1, 1, 3, 7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
            32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301,
            8388593, 16777213, 33554393, 67108859, 134217689, 268435399,
            536870909, 1073741789, 2147483647
    };

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

    private void resize(int capacity) {
        SeparateChainingHashST<Key, Value> t = new SeparateChainingHashST<>(capacity, alpha);
        for (int i = 0; i < M; i++) {
            Node<Key, Value> cur;
            for (cur = st[i]; cur != null; cur = cur.next)
                t.put(cur.key, cur.value); // rehash
        }
        M = t.M;
        logM = t.logM;
        st = t.st;
    }

    private int hash(Key key) {
        int t = key.hashCode() & 0x7fffffff;    // must mask sign bit so as not to get negative hash
        if (logM < 26) t = t % primes[logM + 5];
        return t % M;
    }

    public SeparateChainingHashST(int M, int alpha) {
        this.M = M;
        this.alpha = alpha;
        this.logM = (int) (Math.log(M) / Math.log(2));
        st = (Node<Key, Value>[]) new Node[M];
    }

    public SeparateChainingHashST(int M) {
        this(M, 5);
    }

    public SeparateChainingHashST() {
        this(9997, 5);
    }

    @Override
    public void put(Key key, Value value) {
        if (size / M > alpha) resize(2*M);
        int hash = hash(key);
        Node<Key, Value> cur = st[hash];
        while (cur != null && !cur.key.equals(key)) cur = cur.next;
        if (cur != null) { cur.value = value; return; }
        st[hash] = new Node<>(key, value, st[hash]);
        size++;
    }

    @Override
    public Value get(Key key) { // moving forward
        int hash = hash(key);
        Node<Key, Value> cur = st[hash];
        if (cur == null) return null;
        if (cur.key.equals(key)) return cur.value;
        while (cur.next != null && !cur.next.key.equals(key)) cur = cur.next;
        Node<Key, Value> node = cur.next;
        if (node == null) return null;
        cur.next = node.next;
        node.next = st[hash];
        st[hash] = node;
        return st[hash].value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void delete(Key key) {
        int hash = hash(key);
        if (st[hash] == null) return;
        if (st[hash].key.equals(key)) {
            st[hash] = st[hash].next;
            size--;
        }
        else {
            Node<Key, Value> prev = st[hash];
            while (prev.next != null && !prev.next.key.equals(key)) prev = prev.next;
            if (prev.next != null) {
                prev.next = prev.next.next;
                size--;
            }
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
            return count < size;
        }
        public Key next() {
            Key key = cur.key;
            cur = cur.next;
            count++;
            if (count < size) setCur();
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
        SeparateChainingHashST<String, Integer> st = new SeparateChainingHashST<>(37);
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
