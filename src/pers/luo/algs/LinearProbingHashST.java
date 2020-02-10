package pers.luo.algs;

import java.util.Iterator;
import java.util.Scanner;

@SuppressWarnings("unchecked")
public class LinearProbingHashST<Key, Value> implements ST<Key, Value> {
    private Key[] keys;     // array of keys
    private Value[] values; // array of values
    private int size;       // # of key-value pairs
    private int M;          // # of lists
    private int logM;
    private int occupied;

    private int cache = -1;  // software caching, cannot be null or dead pair

    private final int[] primes = {
            1, 1, 3, 7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
            32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301,
            8388593, 16777213, 33554393, 67108859, 134217689, 268435399,
            536870909, 1073741789, 2147483647
    };

    private int hash(Key key) { // must mask sign bit so as not to get negative hash
        int t = key.hashCode() & 0x7fffffff;
        if (logM < 26) t = t % primes[logM + 5];
        return t % M;
    }

    private void resize(int capacity) {
        LinearProbingHashST<Key, Value> t = new LinearProbingHashST<>(capacity);
        for (int i = 0; i < M; i++)
            if (values[i] != null) t.put(keys[i], values[i]); // rehash
        M = t.M;
        logM = t.logM;
        occupied = t.occupied;
        keys = t.keys;
        values = t.values;
        cache = -1;
    }

    public LinearProbingHashST(int M) {
        this.M = M;
        this.logM = (int) (Math.log(M) / Math.log(2));
        keys   = (Key[])   new Object[M];
        values = (Value[]) new Object[M];
    }

    public LinearProbingHashST() {
        this(9997);
    }

    @Override
    public void put(Key key, Value value) {
        if (2 * occupied >= M) resize(M*2);
        if (cache >= 0 && keys[cache].equals(key)) {
            values[cache] = value;
            return;
        }
        int i = hash(key);
        while (keys[i] != null && !key.equals(keys[i])) {   // linear probing
            i++;
            if (i == M) i = 0;
        }
        if (values[i] == null) size++;
        values[i] = value;
        if (keys[i] == null) {
            keys[i] = key;
            occupied++;
        }
    }

    @Override
    public Value get(Key key) {
        if (cache >= 0 && keys[cache].equals(key)) return values[cache];
        int i = hash(key);
        while (keys[i] != null && !key.equals(keys[i])) {   // linear probing
            i++;
            if (i == M) i = 0;
        }
        if (values[i] != null) cache = i;
        return values[i];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void delete(Key key) {
        int i = hash(key);
        while (keys[i] != null && !keys[i].equals(key)) {
            i++;
            if (i == M) i = 0;
        }
        if (values[i] == null) return;
        values[i] = null;
        size--;
        cache = -1;
        if (size > 0 && 8 * size <= M) resize(M/2);
    }

    @Override
    public Iterable<Key> keys() {
        return new LinearProbingIterable();
    }
    private class LinearProbingIterable implements Iterable<Key> {
        public Iterator<Key> iterator() {
            return new LinearProbingIterator();
        }
    }
    private class LinearProbingIterator implements Iterator<Key> {
        private int i = 0, count = 0;
        public LinearProbingIterator() {
            setIndex();
        }
        public boolean hasNext() {
            return count < size;
        }
        public Key next() {
            Key key = keys[i];
            i++;
            count++;
            if (count < size) setIndex();
            return key;
        }
        private void setIndex() { // set cur to next non-empty slot if cur is null
            if (i == M) i = 0;
            while (values[i] == null) {
                i++;
                if (i == M) i = 0;
            }
        }
    }

    public static void main(String[] args)
    {
        LinearProbingHashST<String, Integer> st = new LinearProbingHashST<>(37);
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; scanner.hasNext(); i++) {
            String key = scanner.next();
            st.put(key, i);
        }
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
        System.out.println("Deletion test:");
        st.delete("E");
        st.delete("R");
        st.delete("L");
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
    }
}
