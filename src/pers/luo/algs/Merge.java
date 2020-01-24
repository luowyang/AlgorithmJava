package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

public class Merge {
    //private static Comparable[] aux;    // auxiliary array used by merge, declared as a static reference

    public static void sort(Comparable[] a, String ver)
    {   // public interface
        if (ver.equals("TD")) sort(a);
        if (ver.equals("BU")) sortBU(a);
    }

    public static void sort(Comparable[] a)
    {   // public interface
        Comparable[] aux = new Comparable[a.length]; // allocate space for auxiliary array
        for (int k = 0; k < a.length; k++)
            aux[k] = a[k];
        sortTD(aux, a, 0, a.length - 1);
    }

    private static void sortTD(Comparable[] src, Comparable[] dest, int lo, int hi)
    {
        if (hi <= lo) return;
        int mid = (lo + hi) / 2;
        sortTD(dest, src, lo, mid);
        sortTD(dest, src, mid+1, hi);
        merge(src, dest, lo, mid, hi);
    }

    private static void sortBU(Comparable[] a)
    {
        Comparable[] aux = new Comparable[a.length]; // allocate space for auxiliary array
        for (int k = 0; k < a.length; k++)
            aux[k] = a[k];
        int N = a.length;
        for (int sz = 1; sz < N; sz += sz)
            for (int lo = 0; lo < N-sz; lo += (sz+sz))
                merge(a, lo, lo+sz-1, Math.min(lo+sz+sz-1, N-1), aux);
    }

    private static void merge(Comparable[] src, Comparable[] dest, int lo, int mid, int hi)
    {   // merge a[lo..mid] & a[mid+1..hi]
        int i = lo, j = mid+1;
        for (int k = lo; k <= hi; k++)
            if      (i > mid)              dest[k] = src[j++]; // i exhausted
            else if (j > hi )              dest[k] = src[i++]; // j exhausted
            else if (less(src[j], src[i])) dest[k] = src[j++]; // a[j] <  a[i]
            else                           dest[k] = src[i++]; // a[i] <= a[j]
    }

    private static void sortNaive(Comparable[] a, int lo, int hi)
    {
        Comparable[] aux = new Comparable[a.length]; // allocate space for auxiliary array
        for (int k = 0; k < a.length; k++)
            aux[k] = a[k];
        if (hi <= lo) return;
        int mid = (lo + hi) / 2;
        sortNaive(a, lo, mid);
        sortNaive(a, mid+1, hi);
        merge(a, lo, mid, hi, aux);
    }

    private static void merge(Comparable[] a, int lo, int mid, int hi, Comparable[] aux)
    {   // merge a[lo..mid] & a[mid+1..hi]
        int i = lo, j = mid+1;
        for (int k = lo; k <= hi; k++)
            aux[k] = a[k];
        for (int k = lo; k <= hi; k++)
            if      (i > mid)              a[k] = aux[j++]; // i exhausted
            else if (j > hi )              a[k] = aux[i++]; // j exhausted
            else if (less(aux[j], aux[i])) a[k] = aux[j++]; // a[j] <  a[i]
            else                           a[k] = aux[i++]; // a[i] <= a[j]
    }

    public static void natural(Comparable[] a)
    {
        int N = a.length;
        int mid, hi;
        Comparable[] aux = new Comparable[N];
        boolean sorted = false;
        while (!sorted) {
            for (int lo = 0; lo < N; lo = hi) {
                for (mid = lo + 1; mid < N && !less(a[mid], a[mid - 1]); mid++) ;
                for (hi = mid + 1; hi < N && !less(a[hi], a[hi - 1]); hi++) ;
                if (mid == N) {
                    if (lo != 0) continue;
                    else {
                        sorted = true;
                        break;
                    }
                }
                merge(a, lo, mid - 1, hi - 1, aux);
            }
        }
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
        //sort(a, "BU");
        natural(a);
        System.out.println("is sorted: " + isSorted(a));
        show(a);
    }
}
