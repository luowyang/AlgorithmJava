package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

public class Heap {
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        // first stage: build heap
        for (int k = N/2-1; k >= 0; k--)
            sink(a, k, N);
        // second stage: sort
        while (N > 1) {
            Util.exch(a, 0, --N);
            sink(a, 0, N);
        }
    }

    public static void sink(Comparable[] a, int k, int N)
    {
        while (left(k) < N) {
            int j = left(k);
            if (j < N-1 && Util.less(a[j], a[j+1])) j++;
            if (!Util.less(a[k], a[j])) break;
            Util.exch(a, k, j);
            k = j;
        }
    }

    public final static int left(int k)
    { return (k << 1) + 1; }
    public final static int right(int k)
    { return (k + 1) << 1; }
    public final static int parent(int k)
    { return (k - 1) >> 1; }

    public static void main(String[] args)
    {
        String[] a = StdIn.readAllStrings();
        sort(a);
        System.out.println("is sorted: " + Util.isSorted(a));
        Util.show(a);
    }
}
