package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Comparator;
import java.util.NoSuchElementException;

/*
* Max priority queue with resizing array and generic type
*/
public class MaxPQ<Key extends Comparable<Key>> {
    private Key[] heap;
    private int N = 0;
    private Comparator<Key> comparator;

    public MaxPQ()
    { heap = (Key[]) new Comparable[1]; }

    public MaxPQ(Comparator<Key> comparator)
    {
        heap = (Key[]) new Comparable[1];
        this.comparator = comparator;
    }

    public MaxPQ(Key[] array)
    {
        if (array == null || array.length == 0) throw new RuntimeException("Null or empty array");
        int sz = 1;
        while (sz < array.length) sz+=sz;
        heap = (Key[]) new Comparable[sz];
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) throw new RuntimeException("Null array item");
            heap[i] = array[i];
        }
        N = array.length;
        for (int k = N/2-1; k >= 0; k--)
            sink(k);
    }

    private final int left(int k)
    { return (k << 1) + 1; }
    private final int right(int k)
    { return (k + 1) << 1; }
    private final int parent(int k)
    { return (k - 1) >> 2; }

    private void resize(int maxN)
    {
        Key[] tmp = (Key[]) new Comparable[maxN];
        for (int i = 0; i < N; i++)
            tmp[i] = heap[i];
        heap = tmp;
    }

    /*private boolean less(int i, int j)
    { return heap[i].compareTo(heap[j]) < 0; }*/

    /*private void exch(int i, int j)
    { Key t = heap[i]; heap[i] = heap[j]; heap[j] = t; }*/

    private void swim(int k)
    {
        Key t = heap[k];
        while (k > 0 && Util.less(heap[parent(k)], t, comparator)) {
            heap[k] = heap[parent(k)];
            k = parent(k);
        }
        heap[k] = t;
    }

    private void sink(int k)
    {
        Key t = heap[k];
        while (left(k) < N) {
            int j = left(k);
            if (j < N-1 && Util.less(heap[left(k)], heap[right(k)], comparator)) j++;
            if (!Util.less(t, heap[j], comparator)) break;
            heap[k] = heap[j];
            k = j;
        }
        heap[k] = t;
    }

    public void insert(Key v)
    {
        if (N == heap.length) resize(N<<1);
        heap[N++] = v;
        swim(N-1);
    }

    public Key delMax()
    {
        if (N == 0) throw new NoSuchElementException("Heap underflow");
        Key max = heap[0];
        heap[0] = heap[--N];
        heap[N] = null; // eliminate obsolete object
        sink(0);
        if (N > 0 && N == heap.length/4) resize(N<<1);
        return max;
    }

    public boolean isEmpty()
    { return N == 0; }

    public int size()
    { return N; }

    public static void main(String[] args)
    {
        MaxPQ<String> pq = new MaxPQ<>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (s.equals("*")) System.out.print(pq.delMax() + " ");
            else               pq.insert(s);
        }
        System.out.println("");
    }
}
