package pers.luo.algs;

import java.util.Scanner;

public class HashSet<Key> {
    private Key[] keys;
    private int size;
    private int M;
    private int logM;

    private final int[] primes = {
            1, 1, 3, 7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
            32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301,
            8388593, 16777213, 33554393, 67108859, 134217689, 268435399,
            536870909, 1073741789, 2147483647
    };

    private int hash(Key key) {
        int hash = key.hashCode() & 0x7fffffff;
        if (logM < 26) hash = hash % primes[logM + 5];
        return hash % M;
    }

    private void resize(int capacity) {
        HashSet<Key> t = (HashSet<Key>) new HashSet(capacity);
        for (int i = 0; i < M; i++)
            if (keys[i] != null) t.add(keys[i]);
        M = t.M;
        logM = t.logM;
        keys = t.keys;
    }

    public HashSet(int M) {
        this.M = M;
        this.logM = (int) (Math.log(M) / Math.log(2));
        this.keys = (Key[]) new Object[M];
    }

    public HashSet() {
        this(997);
    }

    public void add(Key key) {
        if (key == null) throw new IllegalArgumentException("Key must not be null");
        if (M < 2 * size) resize(2 * M);
        int i = hash(key);
        while (keys[i] != null && !keys[i].equals(key))
            if (++i == M) i = 0;
        if (keys[i] != null) return;    // if key already exists, do nothing
        keys[i] = key;
        size++;
    }

    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("Key must not be null");
        int i = hash(key);
        while (keys[i] != null && !keys[i].equals(key))
            if (++i == M) i = 0;
        if (keys[i] == null) return;    // if key does not exist, do nothing
        keys[i++] = null;
        if (i == M) i = 0;
        size--;
        if (size > 0 && 8 * size <= M) {
            resize(M/2);
            return;
        }
        while (keys[i] != null) {
            Key keyToDo = keys[i];  // save keys[i]
            keys[i] = null;         // delete it temporally
            size--;
            add(keyToDo);           // re-insert
            if (++i == M) i = 0;
        }
    }

    public boolean contains(Key key) {
        int i = hash(key);
        while (keys[i] != null && !keys[i].equals(key))
            if (++i == M) i = 0;
        return keys[i] != null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < M; i++)
            if (keys[i] != null) s = s + keys[i] + "\n";
        return s;
    }

    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<>();
        for (int i = 0; i < M; i++)
            if (keys[i] != null) queue.enqueue(keys[i]);
        return queue;
    }

    public static void main(String[] args)
    {
        HashSet<String> set = new HashSet<>(37);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String key = scanner.next();
            set.add(key);
        }
        System.out.println(set);
        System.out.println("Deletion test:");
        set.delete("E");
        set.delete("R");
        set.delete("L");
        System.out.println(set);
    }
}
