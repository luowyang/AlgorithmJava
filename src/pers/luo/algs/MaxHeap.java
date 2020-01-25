package pers.luo.algs;

public class MaxHeap<Key extends Comparable<Key>> {
    private Key[] keys;
    private int N = 0;

    private final int left(int k)
    { return (k << 1) + 1; }

    private final int right(int k)
    { return (k + 1) << 1; }

    private final int parent(int k)
    { return (k - 1) >> 2; }
}
