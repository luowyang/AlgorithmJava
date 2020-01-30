package pers.luo.algs;

import edu.princeton.cs.algs4.StdRandom;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class BST<Key extends Comparable<Key>, Value> implements OrderedST<Key, Value> {
    private Node root;

    private class Node {
        private Key key;
        private Value value;
        private Node left;
        private Node right;
        private int N;
        public Node(Key key, Value value, int N) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.N = N;
        }
    }

    @Override
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("Minimum does not exist");
        Node cur = root;
        while (cur.left != null) cur = cur.left;
        return cur.key;
    }

    @Override
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("Maximum does not exist");
        Node cur = root;
        while (cur.right != null) cur = cur.right;
        return cur.key;
    }

    @Override
    public Key floor(Key key) {
        Key candidate = null;
        Node cur = root;
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if      (cmp > 0) { cur = cur.left;                       }
            else if (cmp < 0) { candidate = cur.key; cur = cur.right; }
            else              { return cur.key;                       }
        }
        return candidate;
    }

    @Override
    public Key ceiling(Key key) {
        Key candidate = null;
        Node cur = root;
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if      (cmp < 0) { cur = cur.right;                       }
            else if (cmp > 0) { candidate = cur.key; cur = cur.left; }
            else              { return cur.key;                       }
        }
        return candidate;
    }

    @Override
    public int rank(Key key) {
        Node cur = root;
        int r = 0;
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if      (cmp > 0) { cur = cur.left;                           }
            else if (cmp < 0) { r += size(cur.left) + 1; cur = cur.right; }
            else              { return r + size(cur.left);                }
        }
        return r;
    }

    @Override
    public Key select(int k) {
        Node cur = root;
        while (cur != null) {
            int t = size(cur.left);
            if      (t > k) { cur = cur.left;              }
            else if (t < k) { k -= t + 1; cur = cur.right; }
            else            { return cur.key;              }
        }
        return null;
    }

    @Override
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Minimum does not exist");
        root = deleteMin(root);
    }
    private Node deleteMin(Node node) {
        if (node.left == null) return node.right;
        node.left = deleteMin(node.left);
        node.N = size(node.left) + size(node.right) + 1;
        return node;
    }

    @Override
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Maximum does not exist");
        root = deleteMax(root);
    }
    private Node deleteMax(Node node) {
        if (node.right == null) return node.left;
        node.right = deleteMin(node.right);
        node.N = size(node.left) + size(node.right) + 1;
        return node;
    }

    @Override
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("Argument of delete() cannot be null");
        if (isEmpty()) return;
        root = delete(root, key);
    }
    private Node delete(Node node, Key key) {
        if (node == null) return null;
        int cmp = node.key.compareTo(key);
        if      (cmp > 0) node.left = delete(node.left, key);
        else if (cmp < 0) node.right = delete(node.right, key);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            // randomly delete using predecessor or successor
            Node cur = node;
            if (StdRandom.uniform(2) == 0) {
                for (node = node.right; node.left != null; node = node.left) ;
                node.right = deleteMin(cur.right);
                node.left = cur.left;
            }
            else {
                for (node = node.left; node.right != null; node = node.right) ;
                node.left = deleteMin(cur.left);
                node.right = cur.right;
            }
        }
        node.N = size(node.left) + size(node.right) + 1;
        return node;
    }

    @Override
    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new Queue<>();
        keys(root, queue, lo, hi);
        return queue;
    }
    private void keys(Node node, Queue<Key> queue, Key lo, Key hi) {
        if (node == null) return;
        int cmplo = node.key.compareTo(lo);
        int cmphi = node.key.compareTo(hi);
        if (cmplo > 0) keys(node.left, queue, lo, hi);
        if (cmplo >= 0 && cmphi <= 0) queue.enqueue(node.key);
        if (cmphi < 0) keys(node.right, queue, lo, hi);
    }

    @Override
    public void put(Key key, Value value) {
        root = put(root, key, value);
    }
    private Node put(Node node, Key key, Value value)
    {   // recursively put key-value pair into sub-tree rooted by "node"
        // return the root of the sub-tree
        if (node == null) return new Node(key, value, 1);
        int cmp = node.key.compareTo(key);
        if      (cmp > 0) { node.left = put(node.left, key, value);   }
        else if (cmp < 0) { node.right = put(node.right, key, value); }
        else              { node.value = value; return node;          }
        node.N = size(node.left) + size(node.right) + 1;
        return node;
    }

    @Override
    public Value get(Key key) {
        Node cur = root;
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if      (cmp > 0) cur = cur.left;
            else if (cmp < 0) cur = cur.right;
            else              return cur.value;
        }
        return null;
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        return node == null ? 0 : node.N;
    }

    public static void main(String[] args)
    {
        BST<String, Integer> st = new BST<>();
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; scanner.hasNext(); i++) {
            String key = scanner.next();
            st.put(key, i);
        }
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
        System.out.println("min      : " + st.min());
        System.out.println("max      : " + st.max());
        System.out.println("floor D  : " + st.floor("D"));
        System.out.println("ceiling D: " + st.ceiling("D"));
        System.out.println("rank N   : " + st.rank("N"));
        System.out.println("select 4 : " + st.select(4));
        st.deleteMin();
        st.deleteMax();
        st.delete("L");
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
    }
}
