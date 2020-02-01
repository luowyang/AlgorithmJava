package pers.luo.algs;

public interface OrderedST<Key extends Comparable<Key>, Value> extends ST<Key, Value> {
    Key min();
    Key max();
    Key floor(Key key);     // round down
    Key ceiling(Key key);   // round up
    int rank(Key key);      // # of keys less than key
    Key select(int k);      // the key that ranked k-th
    default void deleteMin() { delete(min()); }
    default void deleteMax() { delete(max()); }
    default int size(Key lo, Key hi)
    {   // # of keys in range [lo..hi]
        if      (hi.compareTo(lo) < 0)
            return 0;
        else if (contains(hi))
            return rank(hi) - rank(lo) + 1;
        else
            return rank(hi) - rank(lo);
    }
    Iterable<Key> keys(Key lo, Key hi);   // sorted keys in range [lo..hi]
    default Iterable<Key> keys()
    { return keys(min(), max()); }
}
