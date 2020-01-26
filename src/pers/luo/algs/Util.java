package pers.luo.algs;

import java.util.NoSuchElementException;

public final class Util {
    public static boolean less(Comparable v, Comparable w)
    {
        if (v == null || w == null) throw new NoSuchElementException("Element(s) do not exist");
        return v.compareTo(w) < 0;
    }

    public static void show(Comparable[] array)
    {
        if (array == null) throw new RuntimeException("Null array");
        for (Comparable item : array) System.out.print(item + " ");
        System.out.println("");
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
}
