package pers.luo.algs;

import java.util.NoSuchElementException;

/*
* Max priority queue with resizing array and generic type
*/
public class MaxPQ<Key extends Comparable<Key>> {
    private Key[] heap;
    private int N = 0;

    public MaxPQ()
    { heap = (Key[]) new Comparable[1]; }

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

    private boolean less(int i, int j)
    { return heap[i].compareTo(heap[j]) < 0; }

    private void exch(int i, int j)
    { Key t = heap[i]; heap[i] = heap[j]; heap[j] = t; }

    private void swim(int k)
    {
        while (k > 0 && less(parent(k), k)) {
            exch(k, parent(k));
            k = parent(k);
        }
    }

    private void sink(int k)
    {
        while (left(k) < N) {
            int j = left(k);
            if (j < N-1 && less(left(k), right(k))) j++;
            if (!less(k, j)) break;
            exch(k, j);
            k = j;
        }
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

    }
}
