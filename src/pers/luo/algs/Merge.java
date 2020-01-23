package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

public class Merge {
    public static void sort(Comparable[] a)
    {

    }

    public static boolean less(Comparable v, Comparable w)
    { return v.compareTo(w) < 0; }

    public static boolean isSorted(Comparable[] a)
    {
        for (int i = 1; i < a.length; i++)
            if (less(a[i], a[i - 1])) return false;
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
