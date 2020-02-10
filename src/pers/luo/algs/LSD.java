package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

/**
 * Least-Significant-Digit First Radix Sort
 * @author Luo Wenyang
 **/
public class LSD {
    // sort a[] by the first W chars
    public static void sort(String[] a, int W) {
        int N = a.length;               // number of strings
        int R = 256;                    // default charset size
        String[] aux = new String[N];   // auxiliary array
        // do counting sort from LSD to MSD
        for (int d = W-1; d >= 0; d--) {
            int[] count = new int[R+1];                 // counter of frequency
            // counting
            for (String s : a) count[s.charAt(d) + 1]++;        // count[k+1] is the frequency of k
            // accumulating
            for (int r = 0; r < R; r++)
                count[r+1] += count[r];                         // count[k] is the frequency of keys less than k
            // sorting
            for (String s : a) aux[count[s.charAt(d)]++] = s;   // sort to aux[] using count[] as index indicator
            // writing back
            System.arraycopy(aux, 0, a, 0, N);
        }
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        LSD.sort(a, a[0].length());
        for (String s : a)
            System.out.print(s + " ");
        System.out.println();
    }
}
