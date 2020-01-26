package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

public class Selection {
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int min = i;
            for (int j = i + 1; j < N; j++)
                if (Util.less(a[j], a[min])) min = j;
            Util.exch(a, i, min);
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
