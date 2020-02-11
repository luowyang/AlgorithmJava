package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

/**
 * Least-Significant-Digit First Radix Sort
 * @author Luo Wenyang
 **/
public class LSD {
    // map finished string to -1
    private static int charAt(String s, int d) {
        return d < s.length() ? s.charAt(d) : -1;
    }

    // sort a[] by the first W chars
    public static void sort(String[] a, int W) {
        int N = a.length;               // number of strings
        int R = 256;                    // default charset size
        String[] aux = new String[N];   // auxiliary array
        // do counting sort from LSD to MSD
        for (int d = W-1; d >= 0; d--) {
            int[] count = new int[R+2];                 // counter of frequency
            // counting
            for (String s : a) count[charAt(s, d) + 2]++;           // count[r+2] is the frequency of r
            // accumulating
            for (int r = 0; r < R+1; r++)
                count[r+1] += count[r];                             // count[r+1] is the frequency of keys less than r
            // sorting
            for (String s : a) aux[count[charAt(s, d) + 1]++] = s;  // sort to aux[] using count[] as index indicator
            // writing back
            System.arraycopy(aux, 0, a, 0, N);
        }
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        int W = 0;
        for (String s : a) W = Math.max(W, s.length());
        LSD.sort(a, W);
        for (String s : a)
            System.out.print(s + " ");
        System.out.println();
        System.out.println("is sorted: " + Util.isSorted(a));
    }
}
