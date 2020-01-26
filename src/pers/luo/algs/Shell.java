package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

public class Shell {
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        int h = 1;
        while (h < N/3) h = 3*h + 1;    // h_k = 1/2(3^k-1)
        while (h >= 1) {
            for (int i = h; i < N; i++) {
                for (int j = i; j >= h && Util.less(a[j], a[j-h]); j-=h)
                    Util.exch(a, j, j-h);
            }
            h /= 3;
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
