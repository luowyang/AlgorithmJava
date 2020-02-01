package pers.luo.algs;

import java.util.Comparator;
import java.util.NoSuchElementException;

public final class Util {
    public static boolean less(Comparable v, Comparable w)
    {
        if (v == null || w == null) throw new NoSuchElementException("Element(s) do not exist");
        return v.compareTo(w) < 0;
    }

    public static boolean less(Comparable v, Comparable w, Comparator c)
    {
        if (v == null || w == null) throw new NoSuchElementException("Element(s) do not exist");
        if (c == null) return v.compareTo(w)  < 0;
        else           return c.compare(v, w) < 0;
    }

    public static void show(Object[] array)
    {
        if (array == null) throw new RuntimeException("Null array");
        for (Object item : array) System.out.print(item + " ");
        System.out.println("");
    }

    public static boolean isSorted(Comparable[] array, Comparator c)
    {
        if (array == null) return false;
        for (int i = 1; i < array.length; i++) {
            if      (c == null && less(array[i], array[i - 1]))  return false;
            else if (c != null && less(array[i], array[i-1], c)) return false;
        }
        return true;
    }

    public static boolean isSorted(Comparable[] array)
    {
        if (array == null) return false;
        for (int i = 1; i < array.length; i++)
            if (less(array[i], array[i-1])) return false;
        return true;
    }

    public static void exch(Object[] array, int i, int j)
    {
        if (array == null) throw new RuntimeException("Null array");
        if (i < 0 || i >= array.length) throw new NoSuchElementException("array[" + i + "] does not exist");
        if (j < 0 || j >= array.length) throw new NoSuchElementException("array[" + j + "] does not exist");
        Object o = array[i];
        array[i] = array[j];
        array[j] = o;
    }

    private static int permCount(int[] src, int[] dest, int lo, int hi)
    {
        if (hi <= lo) return 0;
        int mid = (lo + hi)/2;
        int count = permCount(dest, src, lo, mid);
        count += permCount(dest, src, mid+1, hi);
        count += permMerge(src, dest, lo, mid, hi);
        return count;
    }

    private static int permMerge(int[] src, int[] dest, int lo, int mid, int hi)
    {
        int i = lo;
        int j = mid + 1;
        int count = 0;
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)          { dest[k] = src[j++]; }
            else if (j > hi )          { dest[k] = src[i++]; count += hi - mid; }
            else if (src[i] <= src[j]) { dest[k] = src[i++]; count += j - mid - 1; }
            else                       { dest[k] = src[j++]; }
        }
        return count;
    }

    public static int kendallTau(int[] a, int[] b)
    {
        int N = a.length;
        int[] map = new int[N];
        for (int i = 0; i < N; i++) map[a[i]] = i;
        int[] bMap = new int[N];
        for (int i = 0; i < N; i++) bMap[i] = map[b[i]];
        int[] aux = new int[N];
        for (int i = 0; i < N; i++) aux[i] = bMap[i];
        return permCount(aux, bMap, 0, N-1);
    }

    public static void main(String[] args)
    {
        int[] a ={0, 3, 1, 6, 2, 5, 4};
        int[] b ={1, 0, 3, 6, 4, 2, 5};
        System.out.println(kendallTau(a, b));
    }
}
