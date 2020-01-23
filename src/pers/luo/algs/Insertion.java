package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

public class Insertion {
    /*public static void sort(Comparable[] a)
    {
        int N = a.length;
        int min = 0;
        for (int k = 1; k < N; k++)
            if (less(a[k], a[min])) min = k;
        exch(a, 0, min);    // a[0] is a sentinel
        for (int i = 2; i < N; i++) {
            Comparable t = a[i];
            int j;
            for (j = i; less(t, a[j - 1]); j--)
                a[j] = a[j - 1];
            a[j] = t;
        }
    }
*/
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        for (int i = 1; i < N; i++) {
            Comparable t = a[i];
            int j;
            for (j = i; j > 0 && less(t, a[j - 1]); j--)
                a[j] = a[j - 1];
            a[j] = t;
        }
    }

    public static boolean less(Comparable v, Comparable w)
    { return v.compareTo(w) < 0; }

    public static void exch(Comparable[] a, int i, int j)
    {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static boolean isSorted(Comparable[] a)
    {
        for (int i = 1; i < a.length; i++) {
            if (less(a[i], a[i - 1]))
                return false;
        }
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
        assert isSorted(a);
        show(a);
    }
}
