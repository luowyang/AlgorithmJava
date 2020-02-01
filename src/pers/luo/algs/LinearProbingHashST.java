package pers.luo.algs;

import java.util.Iterator;
import java.util.Scanner;

@SuppressWarnings("unchecked")
public class LinearProbingHashST<Key, Value> implements ST<Key, Value> {
    private Key[] keys;     // array of keys
    private Value[] values; // array of values
    private int N;          // # of key-value pairs
    private int M;          // # of lists
    private int logM;
    private final int[] primes;
    private final int[] deltas = {1,3,1,5,3,3,9,3,1,3,19,15,1,5,1,3,9,3,15,3,39,5,39,57,3,35,1};

    private int collision = 0;

    private int hash(Key key) { // must mask sign bit so as not to get negative hash
        int t = key.hashCode() & 0x7fffffff;
        if (logM < 26) t = t % primes[logM];
        return t % M;
    }

    private void resize(int capacity) {
        LinearProbingHashST<Key, Value> t = new LinearProbingHashST<>(capacity);
        for (int i = 0; i < M; i++)
            if (keys[i] != null) t.put(keys[i], values[i]);
        M = t.M;
        logM = t.logM;
        keys = t.keys;
        values = t.values;
    }

    public LinearProbingHashST(int M) {
        this.M = M;
        this.logM = (int) Math.log(M);
        keys   = (Key[])   new Object[M];
        values = (Value[]) new Object[M];
        primes = new int[deltas.length];
        int i = 32;
        for (int k = 0; k < deltas.length; k++) {
            primes[k] = i - deltas[k];
            i = i << 1;
        }
    }

    public LinearProbingHashST() {
        this(9997);
    }

    @Override
    public void put(Key key, Value value) {
        if (N >= M / 2) resize(M*2);
        int i = hash(key);
        while (keys[i] != null && !key.equals(keys[i])) {   // linear probing
            i++;
            if (i == M) i = 0;
            collision++;
        }
        values[i] = value;
        if (keys[i] == null) {
            keys[i] = key;
            N++;
        }
    }

    @Override
    public Value get(Key key) {
        int i = hash(key);
        while (keys[i] != null && !key.equals(keys[i])) {   // linear probing
            i++;
            if (i == M) i = 0;
        }
        if (keys[i] == null) return null;
        return values[i];
    }

    @Override
    public int size() {
        return N;
    }

    @Override
    public void delete(Key key) {
        int hash = hash(key);
        int i = hash;
        while (keys[i] != null && !keys[i].equals(key)) i = (i + 1) % M;
        if (keys[i] == null) return;
        keys[i] = null;
        values[i] = null;
        i = (i + 1) % M;
        while (keys[++i] != null) {
            Key keyToReDo = keys[i];
            Value valueToReDo = values[i];
            keys[i] = null;
            values[i] = null;
            N--;
            put(keyToReDo, valueToReDo);
            i = (i + 1) % M;
        }
        N--;
        if (N > 0 && N <= M / 8) resize(M/2);
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
            return count < N;
        }
        public Key next() {
            Key key = keys[i];
            i++;
            count++;
            if (count < N) setIndex();
            return key;
        }
        private void setIndex() { // set cur to next non-empty slot if cur is null
            if (i == M) i = 0;
            while (keys[i] == null) {
                i++;
                if (i == M) i = 0;
            }
        }
    }

    public static void main(String[] args)
    {
        LinearProbingHashST<String, Integer> st = new LinearProbingHashST<>();
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; scanner.hasNext(); i++) {
            String key = scanner.next();
            st.put(key, i);
        }
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
        System.out.println("Number of collisions: " + st.collision);
        System.out.println("Deletion test:");
        st.delete("E");
        st.delete("R");
        st.delete("L");
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
    }
}
