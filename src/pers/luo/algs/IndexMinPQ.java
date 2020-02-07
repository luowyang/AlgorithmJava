package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.NoSuchElementException;

public class IndexMinPQ<Item extends Comparable<Item>> {
    private Item[] keys;    // key of item indexed by index
    private int[] pos;      // position of item in heap indexed by index
    private int[] heap;     // heap containing indexes of items as links
    private int N = 0;

    public IndexMinPQ(int maxN)
    {
        keys = (Item[]) new Comparable[maxN + 1];
        pos = new int[maxN + 1];
        heap = new int[maxN + 1];
    }

    public void insert(int index, Item item)
    {
        if (contains(index)) throw new RuntimeException("Index " + index + " already exists");
        keys[index] = item;
        heap[N++] = index;
        swim(N-1);
    }

    public void change(int index, Item item)
    {
        if (!contains(index)) throw new NoSuchElementException("Index " + index + " does not exists");
        keys[index] = item;
        swim(pos[index]);
        sink(pos[index]);
    }

    public boolean contains(int k)
    { return keys[k] != null; }

    public void delete(int index)
    {
        heap[pos[index]] = heap[--N];
        swim(pos[index]);
        sink(pos[index]);
        keys[index] = null;
    }

    public Item min()
    {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return keys[heap[0]];
    }

    public int minIndex()
    {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return heap[0];
    }

    public int delMin()
    {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        int indexOfMin = heap[0];
        keys[indexOfMin] = null;
        heap[0] = heap[--N];
        swim(0);
        sink(0);
        return indexOfMin;
    }

    public boolean isEmpty()
    { return N == 0; }

    public int size()
    { return N; }

    private final int parent(int k)
    { return (k - 1) / 2; }
    private final int left(int k)
    { return (k * 2) + 1; }
    private final int right(int k)
    { return (k + 1) * 2; }

    private void sink(int k)
    {
        int t = heap[k];
        while (left(k) < N) {
            int j = left(k);
            if (j < N-1 && Util.less(keys[heap[j+1]], keys[heap[j]])) j++;
            if (!Util.less(keys[heap[j]], keys[t])) break;
            heap[k] = heap[j];
            pos[heap[j]] = k;
            k = j;
        }
        heap[k] = t;
        pos[t] = k;
    }

    private void swim(int k)
    {
        int t = heap[k];
        while (k > 0 && Util.less(keys[t], keys[heap[parent(k)]])) {
            heap[k] = heap[parent(k)];
            pos[heap[parent(k)]] = k;
            k = parent(k);
        }
        heap[k] = t;
        pos[t] = k;
    }

    public static void main(String[] args)
    {
        IndexMinPQ<String> pq = new IndexMinPQ<>(20);
        String[] ss;
        int i = 0;
        ss = StdIn.readAllStrings();
        for (String s : ss) {
            if (s.equals("*")) System.out.print(pq.delMin() + " ");
            else               pq.insert(i++, s);
        }
        System.out.println("");
        for (String s : ss)
            System.out.print(s + " ");
        System.out.println("");
    }
}
