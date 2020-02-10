package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

/**
 * Most-Significant-Digit First Radix Sort
 * @author Luo Wenyang
 **/
public class MSD {
    private static int R = 256;     // radix
    private static String[] aux;    // auxiliary array

    private static int M = 10;      // switching threshold

    // return -1 if index out of bound
    private static int charAt(String s, int d) {
        return d < s.length() ? s.charAt(d) : -1;
    }

    public static void sort(String[] a) {
        int N = a.length;
        aux = new String[N];
        sort(a, 0, N-1, 0);
    }

    // counting sort a[lo..hi] with d-th highest character
    private static void sort(String[] a, int lo, int hi, int d) {
        if (hi <= lo + M) { // switch to insertion sort for small sub-arrays
            insertionSort(a, lo, hi, d);
            return;
        }
        // counting sort
        int[] count = new int[R+2];
        for (int i = lo; i <= hi; i++)
            count[charAt(a[i], d) + 2]++;
        for (int r = 0; r < R+1; r++)
            count[r+1] += count[r];
        for (int i = lo; i <= hi; i++)
            aux[count[charAt(a[i], d) + 1]++] = a[i];
        for (int i = lo; i <= hi; i++)
            a[i] = aux[i - lo];
        // recursive calls on R possible sub-arrays
        for (int r = 0; r < R; r++)
            sort(a, lo + count[r], lo + count[r+1] - 1, d+1);
    }

    // insertion sort for small arrays
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
        MSD.sort(a);
        for (String s : a)
            System.out.print(s + " ");
        System.out.println();
        System.out.println("is sorted: " + Util.isSorted(a));
    }
}
