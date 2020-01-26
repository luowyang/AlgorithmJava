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
        Comparable t;
        for (int i = 1; i < N; i++) {
            t = a[i];
            int j;
            for (j = i; j > 0 && Util.less(t, a[j - 1]); j--)
                a[j] = a[j - 1];
            a[j] = t;
        }
    }

    public static void sort(Comparable[] a, int lo, int hi)
    {
        Comparable t;
        for (int i = lo+1; i <= hi; i++) {
            t = a[i];
            int j;
            for (j = i; j > lo && Util.less(t, a[j - 1]); j--)
                a[j] = a[j - 1];
            a[j] = t;
        }
    }

    public static void main(String[] args)
    {
        String[] a = StdIn.readAllStrings();
        sort(a);
        assert Util.isSorted(a);
        Util.show(a);
    }
}
