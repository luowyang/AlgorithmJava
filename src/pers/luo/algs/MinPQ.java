package pers.luo.algs;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Scanner;

@SuppressWarnings("unchecked")
public class MinPQ<Key extends Comparable<Key>> {
    private Key[] heap;
    private int N = 0;
    private Comparator<Key> comparator;

    public MinPQ(int capacity) {
        heap = (Key[]) new Comparable[capacity + 1];
    }

    public MinPQ() {
        this(1);
    }

    public MinPQ(Comparator<Key> comparator)
    {
        this(1);
        this.comparator = comparator;
    }

    public MinPQ(Key[] array)
    {
        if (array == null || array.length == 0) throw new RuntimeException("Null or empty array");
        int sz = 1;
        while (sz < array.length) sz += sz;
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
    { return (k * 2) + 1; }
    private final int right(int k)
    { return (k + 1) * 2; }
    private final int parent(int k)
    { return (k - 1) / 2; }

    private void resize(int maxN)
    {
        Key[] tmp = (Key[]) new Comparable[maxN];
        for (int i = 0; i < N; i++)
            tmp[i] = heap[i];
        heap = tmp;
    }

    private void swim(int k)
    {
        Key t = heap[k];
        while (k > 0 && Util.less(t, heap[parent(k)], comparator)) {
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
            if (j < N-1 && Util.less(heap[right(k)], heap[left(k)], comparator)) j++;
            if (!Util.less(heap[j], t, comparator)) break;
            heap[k] = heap[j];
            k = j;
        }
        heap[k] = t;
    }

    public void insert(Key v)
    {
        if (N == heap.length - 1) resize(heap.length*2);
        heap[N++] = v;
        swim(N-1);
    }

    public Key delMin()
    {
        if (N == 0) throw new NoSuchElementException("Heap underflow");
        Key min = heap[0];
        heap[0] = heap[--N];
        heap[N] = null; // eliminate obsolete object
        sink(0);
        if (N > 0 && N == (heap.length-1)/4) resize(heap.length/2);
        return min;
    }

    public boolean isEmpty()
    { return N == 0; }

    public int size()
    { return N; }

    public static void main(String[] args)
    {
        MinPQ<String> pq = new MinPQ<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.next();
            if (s.equals("*")) System.out.print(pq.delMin() + " ");
            else               pq.insert(s);
        }
        System.out.println("");
    }
}
