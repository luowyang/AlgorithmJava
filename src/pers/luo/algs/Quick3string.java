package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

/**
 * Three-way Partition String Quick Sort
 * @author Luo Wenyang
 **/
public class Quick3string {
    private static int M = 10;  // switching threshold

    // convert finished strings to -1 so they will be less than any unfinished string
    private static int charAt(String s, int d) {
        return d < s.length() ? s.charAt(d) : -1;
    }

    private static void exch(String[] a, int i, int j) {
        String s = a[i];
        a[i] = a[j];
        a[j] = s;
    }

    public static void sort(String[] a) {
        sort(a, 0, a.length-1, 0);
    }

    // sort a[lo..hi] by d-th highest digit
    private static void sort(String[] a, int lo, int hi, int d) {
        if (hi <= lo + M) {
            insertionSort(a, lo, hi, d);
            return;
        }
        int v = charAt(a[lo], d);   // pivot is the d-th highest digit of the first string
        int lt = lo - 1;            // end of lesser section
        int gt = hi + 1;            // start of greater section
        int i = lo;                 // string to check, also end of equal section + 1
        while (i < gt) {            // partitioning
            int t = charAt(a[i], d);
            if      (t < v) exch(a, ++lt, i++);     // we already know a[lt+1] is equal to the pivot
            else if (t > v) exch(a, --gt, i);       // note that we have not checked a[gt-1] yet
            else            i++;                    // if a[i] is equal to the pivot, just expand the equal section
        }
        // a[lo..lt] < a[lt+1..gt-1] < a[gt..hi]
        sort(a, lo, lt, d);                                 // recursive call on lesser section
        if (v >= 0) sort(a, lt+1, gt-1, d+1);    // recursive call on equal section if the pivot is not -1
        sort(a, gt, hi, d);                                 // recursive call on greater section
    }

    // string insertion sort of a[lo..hi]
    // assuming the highest d digits are the same
    private static void insertionSort(String[] a, int lo, int hi, int d) {
        for (int i = lo; i < hi; i++) {
            int j = i + 1;
            String s = a[j];
            while (j > lo && less(s, a[j-1], d)) { a[j] = a[j-1]; j--; }
            a[j] = s;
        }
    }

    private static boolean less(String v, String w, int d) {
        return v.substring(d).compareTo(w.substring(d)) < 0;
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        Quick3string.sort(a);
        for (String s : a)
            System.out.print(s + " ");
        System.out.println();
        System.out.println("is sorted: " + Util.isSorted(a));
    }
}
