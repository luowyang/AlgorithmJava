package pers.luo.algs;

public interface StringST<Value> {
    Value get(String key);
    void put(String key, Value value);
    void delete(String key);
    int size();
    boolean isEmpty();
    boolean contains(String key);
    Iterable<String> keys();
    Iterable<String> keysWithPrefix(String pre);
    Iterable<String> keysThatMatch(String pat);
    String longestPrefixOf(String s);
}
