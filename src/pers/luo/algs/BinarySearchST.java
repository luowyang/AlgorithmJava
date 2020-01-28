package pers.luo.algs;

import java.util.Iterator;
import java.util.Scanner;

public class BinarySearchST<Key extends Comparable<Key>, Value> implements OrderedST<Key, Value> {
    private Key[] keys;
    private Value[] values;
    private int N;

    public BinarySearchST() {
        keys = (Key[]) new Comparable[1];
        values = (Value[]) new Object[1];
    }

    private void resize(int maxN)
    {
        Key[] tmp1 = (Key[]) new Comparable[maxN];
        for (int i = 0; i < N; i++)
            tmp1[i] = keys[i];
        keys = tmp1;
        Value[] tmp2 = (Value[]) new Object[maxN];
        for (int i = 0; i < N; i++)
            tmp2[i] = values[i];
        values = tmp2;
    }

    private int binarySearch(Key key, int lo, int hi)
    {
        while (lo <= hi) {
            int mid = lo + (hi - lo)/2;
            if      (keys[mid].compareTo(key) > 0) hi = mid - 1;
            else if (keys[mid].compareTo(key) < 0) lo = mid + 1;
            else                                   return mid;
        }
        return -1;
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
        int lo = 0;
        int hi = N - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo)/2;
            if      (keys[mid].compareTo(key) > 0) hi = mid - 1;
            else if (keys[mid].compareTo(key) < 0) lo = mid + 1;
            else                                   return keys[mid];
        }
        return hi < 0 ? null : keys[hi];
    }

    @Override
    public Key ceiling(Key key) {
        int lo = 0;
        int hi = N - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo)/2;
            if      (keys[mid].compareTo(key) > 0) hi = mid - 1;
            else if (keys[mid].compareTo(key) < 0) lo = mid + 1;
            else                                   return keys[mid];
        }
        return lo >= N ? null : keys[lo];
    }

    @Override
    public int rank(Key key) {
        int lo = 0;
        int hi = N - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo)/2;
            if      (keys[mid].compareTo(key) > 0) hi = mid - 1;
            else if (keys[mid].compareTo(key) < 0) lo = mid + 1;
            else                                   return mid;
        }
        return hi + 1;
    }

    @Override
    public Key select(int k) {
        return (k < 0 || k >= N) ? null : keys[k];
    }

    @Override
    public Iterable<Key> keys(Key lo, Key hi) {
        return new ArrayIterable();
    }
    private class ArrayIterable implements Iterable<Key> {
        public Iterator<Key> iterator()
        { return new ArrayIterator(); }
    }
    private class ArrayIterator implements Iterator<Key> {
        private int cur = 0;
        public boolean hasNext()
        { return cur < N; }
        public Key next()
        { return keys[cur++]; }
    }

    @Override
    public void put(Key key, Value value) {
        int pos = binarySearch(key, 0, N-1);
        if (pos >= 0) { values[pos] = value; return; }  // if hit, reset value
        if (keys.length == N) resize(2*N);
        int k;
        for (k = N; k > 0  && key.compareTo(keys[k-1]) < 0; k--)
        { keys[k] = keys[k-1]; values[k] = values[k-1]; }
        keys[k] = key;
        values[k] = value;
        N++;
    }

    @Override
    public Value get(Key key) {
        int pos = binarySearch(key, 0, N-1);
        return pos>=0 ? values[pos] : null;
    }

    @Override
    public void delete(Key key) {
        int pos = binarySearch(key, 0, N-1);
        if (pos < 0) return;
        for (int i = pos; i < N-1; i++) {
            keys[i] = keys[i+1];
            values[i] = values[i+1];
        }
        keys[N-1] = null;
        values[N-1] = null;
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
        System.out.println("max      : " + st.max());
        System.out.println("min      : " + st.min());
        System.out.println("floor D  : " + st.floor("D"));
        System.out.println("ceiling D: " + st.ceiling("D"));
        System.out.println("rank N   : " + st.rank("N"));
        System.out.println("select 4 : " + st.select(4));
    }
}
