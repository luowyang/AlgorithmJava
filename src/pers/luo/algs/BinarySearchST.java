package pers.luo.algs;

import java.util.Iterator;
import java.util.Scanner;

public class BinarySearchST<Key extends Comparable<Key>, Value> implements OrderedST<Key, Value> {
    private Key[] keys;
    private Value[] values;
    private int N;
    private int cache = 0;

    public BinarySearchST() {
        keys = (Key[]) new Comparable[2];
        values = (Value[]) new Object[2];
    }

    public BinarySearchST(int maxN) {
        keys = (Key[]) new Comparable[maxN];
        values = (Value[]) new Object[maxN];
    }

    private void resize(int capacity)
    {
        Key[] tmp1 = (Key[]) new Comparable[capacity];
        for (int i = 0; i < N; i++)
            tmp1[i] = keys[i];
        keys = tmp1;
        Value[] tmp2 = (Value[]) new Object[capacity];
        for (int i = 0; i < N; i++)
            tmp2[i] = values[i];
        values = tmp2;
    }

    @Override
    public Key min() {
        return isEmpty() ? null : keys[0];
    }

    @Override
    public Key max() {
        return isEmpty() ? null : keys[N-1];
    }

    @Override
    public Key floor(Key key) {
        int pos = rank(key);
        if (pos < N && keys[pos].compareTo(key) == 0) return keys[pos];
        if (pos > 0) return keys[pos-1];
        return null;
    }

    @Override
    public Key ceiling(Key key) {
        int pos = rank(key);
        return (pos < N) ? keys[pos] : null;
    }

    @Override
    public int rank(Key key) {
        if (cache < N && keys[cache].compareTo(key) == 0) return cache;
        int lo = 0;
        int hi = N - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo)/2;
            int cmp = keys[mid].compareTo(key);
            if      (cmp > 0) hi = mid - 1;
            else if (cmp < 0) lo = mid + 1;
            else              {cache = mid; return mid; }
        }
        return lo;
    }

    @Override
    public Key select(int k) {
        return (k < 0 || k >= N) ? null : keys[k];
    }

    @Override
    public Iterable<Key> keys(Key lo, Key hi) {
        int low = rank(lo);
        int high = rank(hi);
        if (!contains(hi)) high--;
        return new ArrayIterable(low, high);
    }
    private class ArrayIterable implements Iterable<Key> {
        private int lo;
        private int hi;
        public ArrayIterable(int lo, int hi) {
            this.lo = lo;
            this.hi = hi;
        }
        public Iterator<Key> iterator()
        { return new ArrayIterator(lo, hi); }
    }
    private class ArrayIterator implements Iterator<Key> {
        private int cur;
        private int hi;
        ArrayIterator(int lo, int hi) {
            cur = lo;
            this.hi = hi;
        }
        public boolean hasNext()
        { return cur <= hi; }
        public Key next()
        { return keys[cur++]; }
    }

    @Override
    public void put(Key key, Value value) {
        int pos = rank(key);
        if (pos < N && keys[pos].compareTo(key) == 0) { values[pos] = value; return; }  // if hit, reset value
        if (keys.length == N) resize(2*N);
        int k;
        for (k = N; k > pos; k--)
        { keys[k] = keys[k-1]; values[k] = values[k-1]; }
        keys[k] = key;
        values[k] = value;
        N++;
    }

    @Override
    public Value get(Key key) {
        int pos = rank(key);
        return (pos < N && keys[pos].compareTo(key) == 0) ? values[pos] : null;
    }

    @Override
    public void delete(Key key) {
        int pos = rank(key);
        if (pos >= N || keys[pos].compareTo(key) != 0) return;
        for (int i = pos; i < N-1; i++) {
            keys[i] = keys[i+1];
            values[i] = values[i+1];
        }
        N--;
        keys[N] = null;
        values[N] = null;
    }

    @Override
    public int size() {
        return N;
    }

    public static void main(String[] args)
    {
        BinarySearchST<String, Integer> st = new BinarySearchST<>();
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; scanner.hasNext(); i++) {
            String key = scanner.next();
            st.put(key, i);
        }
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
        System.out.println("min      : " + st.min());
        System.out.println("max      : " + st.max());
        System.out.println("floor D  : " + st.floor("D"));
        System.out.println("ceiling D: " + st.ceiling("D"));
        System.out.println("rank N   : " + st.rank("N"));
        System.out.println("select 4 : " + st.select(4));
        st.deleteMin();
        st.deleteMax();
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
    }
}
