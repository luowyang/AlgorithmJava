package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

public class Quick {
    public static void sort(Comparable[] a)
    {
        sort(a, 0, a.length-1);
    }

    private static void sort(Comparable[] a, int lo, int hi)
    {
        if (lo >= hi) return;   // basic situation
        int k = partition(a, lo, hi);
        sort(a, lo, k-1);
        sort(a, k+1, hi);
    }

    private static int partition(Comparable[] a, int lo, int hi)
    {
        int i = lo;
        int j = lo + 1;
        Comparable key = a[lo];
        while (j <= hi) {
            if (less(a[j], key)) exch(a, ++i, j++);
            else                 j++;
        }
        exch(a, lo, i);
        return i;
    }

    public static void exch(Comparable[] a, int i, int j)
    {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static boolean less(Comparable v, Comparable w)
    { return v.compareTo(w) < 0;}

    public static boolean isSorted(Comparable[] a)
    {
        for (int i = 1; i < a.length; i++)
            if (less(a[i], a[i-1])) return false;
        return true;
    }

    public static void show(Comparable[] a)
    {
        for (int i = 0; i < a.length; i++)
            System.out.print(a[i] + " ");
        System.out.println("");
    }

    public static void main(String[] args)
    {
        String[] a = StdIn.readAllStrings();
        sort(a);
        System.out.println("is sorted: " + isSorted(a));
        show(a);
    }
}
