package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Comparator;

public class Quick {
    static final int M = 9;

    public static void sort(Comparable[] a)
    {
//        sort(a, 0, a.length-1);
        sort3way(a, 0, a.length-1);
    }

    private static void sort(Comparable[] a, int lo, int hi)
    {
        if (lo + M >= hi) { Insertion.sort(a, lo, hi); return; }   // basic situation
        int k = medianPartition(a, lo, hi);
        sort(a, lo, k-1);
        sort(a, k+1, hi);
    }

    private static void sort3way(Comparable[] a, int lo, int hi)
    {
        if (lo + M >= hi) { Insertion.sort(a, lo, hi); return; }   // basic situation
        int lt = lo - 1;
        int gt = hi + 1;
        int i = lo;
        Comparable pivot = a[lo];
        while (i < gt) {
            int cmp = a[i].compareTo(pivot);
            if      (cmp < 0) Util.exch(a, ++lt, i++);
            else if (cmp > 0) Util.exch(a, --gt, i);
            else                                i++;
        }
        sort3way(a, lo, lt);
        sort3way(a, gt, hi);
    }

    public static Comparable select(Comparable[] a, int k)
    {   // find the kth sequential statistic, k starts from 0
        // return selectLoop(a, k);
        return selectRecursive(a, 0, a.length-1, k);
    }

    private static Comparable selectLoop(Comparable[] a, int k)
    {
        int lo = 0;
        int hi = a.length - 1;
        while (lo < hi) {
            int j = partition(a, lo, hi);
            if      (j == k)  return a[j];
            else if (j > k )  hi = j - 1;
            else              lo = j + 1;
        }
        return a[k];
    }

    private static Comparable selectRecursive(Comparable[] a, int lo, int hi, int k)
    {
        if (lo == hi) return a[lo];
        int j = partition(a, lo, hi);
        if      (j == k) return a[j];
        else if (j > k ) return selectRecursive(a, lo, j-1, k);
        else             return selectRecursive(a, j+1, hi, k);
    }

    private static int partition(Comparable[] a, int lo, int hi)
    {
        /*int i = lo;
        int j = lo + 1;
        Comparable pivot = a[lo];
        while (j <= hi) {
            if (less(a[j], pivot)) exch(a, ++i, j++);
            else                 j++;
        }
        exch(a, lo, i);
        return i;*/
        int i = lo;
        int j = hi + 1;
        Comparable pivot = a[lo];
        while (true) {
            while (a[++i].compareTo(pivot) < 0) if (i == hi) break;
            while (a[--j].compareTo(pivot) > 0) if (j == lo) break;
            if (i >= j) break;
            Util.exch(a, i, j);
        }
        Util.exch(a, lo, j);
        return j;
    }

    private static int randomPartition(Comparable[] a, int lo, int hi)
    {
        int key = StdRandom.uniform(lo, hi+1);
        Comparable pivot = a[key];
        Util.exch(a, lo, key);
        int i = lo;
        int j = lo + 1;
        while (j <= hi) {
            if (Util.less(a[j], pivot)) Util.exch(a, ++i, j++);
            else                   j++;
        }
        Util.exch(a, lo, i);
        return i;
    }

    private static int medianPartition(Comparable[] a, int lo, int hi)
    {
        int left, right, median;
        if (a[lo].compareTo(a[lo+1]) <= 0) { left = lo; right = lo+1; }
        else                               { left = lo+1; right = lo; }
        if      (a[lo+2].compareTo(a[left]) <= 0 ) median = left;
        else if (a[lo+2].compareTo(a[right]) >= 0) median = right;
        else                                    median = lo+2;
        Comparable pivot = a[median];
        Util.exch(a, lo, median);
        int i = lo;
        int j = hi + 1;
        while (true) {
            while (a[++i].compareTo(pivot) < 0) if(i == hi) break;
            while (a[--j].compareTo(pivot) > 0) if(j == lo) break;
            if (i >= j) break;
            Util.exch(a, i, j);
        }
        Util.exch(a, lo, j);
        return j;
    }

    public static void main(String[] args)
    {
        String[] a = StdIn.readAllStrings();
        //int k = (a.length - 1)/2;
        //System.out.println(k + "-th sequential statistic is " + select(a, k));
        sort(a);
        System.out.println("is sorted: " + Util.isSorted(a));
        Util.show(a);
    }
}

