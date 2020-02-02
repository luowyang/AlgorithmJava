package pers.luo.algs;

import java.util.Iterator;
import java.util.Scanner;

public class AVLSCHST<Key extends Comparable<Key>, Value> implements ST<Key, Value> {
    private AVLBST<Key, Value>[] st;  // array of lists
    private int size;                 // # of key-value pairs
    private int M;                    // # of lists
    private int logM;

    private final int[] primes = {
            1, 1, 3, 7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
            32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301,
            8388593, 16777213, 33554393, 67108859, 134217689, 268435399,
            536870909, 1073741789, 2147483647
    };

    private void resize(int capacity) {
        AVLSCHST<Key, Value> t = new AVLSCHST<>(capacity);
        for (int i = 0; i < M; i++) {
            for (Key key : st[i].keys())
                t.put(key, st[i].get(key)); // rehash
        }
        size = t.size;
        M = t.M;
        logM = t.logM;
        st = t.st;
    }

    private int hash(Key key) {
        int t = key.hashCode() & 0x7fffffff;    // must mask sign bit so as not to get negative hash
        if (logM < 26) t = t % primes[logM + 5];
        return t % M;
    }

    public AVLSCHST(int M) {
        this.M = M;
        this.logM = (int) (Math.log(M) / Math.log(2));
        st = (AVLBST<Key, Value>[]) new AVLBST[M];
        for (int i = 0; i < M; i++)
            st[i] = new AVLBST<>();
    }

    public AVLSCHST() {
        this(9997);
    }

    @Override
    public void put(Key key, Value value) {
        if (size / M > 5) resize(2*M);
        int hash = hash(key);
        if (!st[hash].contains(key)) size++;
        st[hash].put(key, value);
    }

    @Override
    public Value get(Key key) {
        return st[hash(key)].get(key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void delete(Key key) {
        st[hash(key)].delete(key);
        size--;
    }

    @Override
    public Iterable<Key> keys() {
        return new AVLSCHSTIterable();
    }
    private class AVLSCHSTIterable implements Iterable<Key> {
        public Iterator<Key> iterator() {
            return new AVLSCHSTIterator();
        }
    }
    private class AVLSCHSTIterator implements Iterator<Key> {
        private int i = 0, count = 0;
        Iterator<Key> cur = st[0].keys().iterator();
        public boolean hasNext() {
            return count < size;
        }
        public Key next() {
            if (count < size) setCur();
            Key key = cur.next();
            count++;
            return key;
        }
        private void setCur() { // set cur to next non-empty slot if cur is null
            if (!cur.hasNext()) {
                i++;
                while (st[i].isEmpty()) i++;
                cur = st[i].keys().iterator();
            }
        }
    }

    public static void main(String[] args)
    {
        AVLSCHST<String, Integer> st = new AVLSCHST<>(37);
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
