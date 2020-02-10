package pers.luo.algs;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Set<Key extends Comparable<Key>> {
    private Node root;
    private Node cache; // software caching

    private class Node {
        Key key;
        Node left, right;
        int size;
        int height;
        public Node(Key key, int size, int height) {
            this.key = key;
            this.left = null;
            this.right = null;
            this.size = size;
            this.height = height;
        }
    }

    // helper to avoid judging null explicitly
    private int size(Node node) {
        return node == null ? 0 : node.size;
    }

    // helper to avoid judging null explicitly
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private Node rotateLeft(Node node) {
        Node x = node.right;
        // set links
        node.right = x.left;
        x.left = node;
        // set heights
        node.height = 1 + Math.max(height(node.left), height(node.right));
        x.height = 1 + Math.max(height(node), height(x.right));
        // set sizes
        x.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return x;
    }

    private Node rotateRight(Node node) {
        Node x = node.left;
        // set links
        node.left = x.right;
        x.right = node;
        // set heights
        node.height = 1 + Math.max(height(node.left), height(node.right));
        x.height = 1 + Math.max(height(node), height(x.left));
        // set sizes
        x.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return x;
    }

    private int balanceFactor(Node node) {
        if (node == null) return 0; // necessary because child would be called and it could be null
        return height(node.left) - height(node.right);
    }

    private Node balance(Node node) {
        if (balanceFactor(node) > 1) {
            if (balanceFactor(node.left) < 0) node.left = rotateLeft(node.left);  // case 2
            node = rotateRight(node);   // case 1
        }
        if (balanceFactor(node) < -1) {
            if (balanceFactor(node.right) > 0) node.right = rotateRight(node.right);  // case 3
            node = rotateLeft(node);   // case 4
        }
        return node;
    }

    public Key min() {
        if (isEmpty()) return null;
        return min(root).key;
    }
    private Node min(Node node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;
        return node;
    }

    public Key max() {
        if (isEmpty()) return null;
        return max(root).key;
    }
    private Node max(Node node) {
        if (node == null) return null;
        while (node.right != null) node = node.right;
        return node;
    }

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

    public int rank(Key key) {
        Node cur = root;
        int r = 0;          // r stores # of known nodes less than key
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if      (cmp > 0) { cur = cur.left;                           }
            else if (cmp < 0) { r += size(cur.left) + 1; cur = cur.right; }
            else              { cache = cur; return r + size(cur.left);   }
        }
        return r;
    }

    public Key select(int k) {
        Node cur = root;
        while (cur != null) {
            int t = size(cur.left);
            if      (t > k) { cur = cur.left;              }
            else if (t < k) { k -= t + 1; cur = cur.right; }
            else            { cache = cur; return cur.key; }
        }
        return null;    // will trigger only if k is out of bound
    }

    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new Queue<>();
        keys(root, queue, lo, hi);
        return queue;
    }
    private void keys(Node node, Queue<Key> queue, Key lo, Key hi) {
        if (node == null) return;   // basic case
        int cmplo = lo.compareTo(node.key);
        int cmphi = hi.compareTo(node.key);
        if (cmplo < 0) keys(node.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(node.key);
        if (cmphi > 0) keys(node.right, queue, lo, hi);
    }

    public void add(Key key) {
        if (cache != null && cache.key.compareTo(key) == 0) return;
        root = add(root, key);
    }
    private Node add(Node node, Key key) {
        if (node == null) {
            cache = new Node(key, 1, 1);
            return cache;
        }
        int cmp = key.compareTo(node.key);
        if      (cmp < 0) node.left  = add(node.left, key);
        else if (cmp > 0) node.right = add(node.right, key);
        else              return node;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        node.size = 1 + size(node.left) + size(node.right);
        return balance(node);
    }

    public boolean contains(Key key) {
        if (cache != null && cache.key.compareTo(key) == 0) return true;
        Node cur = root;
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if      (cmp > 0) cur = cur.left;
            else if (cmp < 0) cur = cur.right;
            else { cache = cur; return true; }
        }
        return false;
    }

    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Minimum does not exist");
        root = deleteMin(root);
    }
    // delete min from subtree rooted by node while maintaining AVL properties
    private Node deleteMin(Node node) {
        if (node.left == null) {
            if (cache == node) cache = null;
            return node.right;
        }
        node.left = deleteMin(node.left);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        node.size = size(node.left) + size(node.right) + 1;
        return balance(node);
    }

    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Maximum does not exist");
        root = deleteMax(root);
    }
    // delete max from subtree rooted by node while maintaining AVL properties
    private Node deleteMax(Node node) {
        if (node.right == null){
            if (cache == node) cache = null;
            return node.left;
        }
        node.right = deleteMax(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        node.size = size(node.left) + size(node.right) + 1;
        return balance(node);
    }

    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("Argument of delete() cannot be null");
        if (isEmpty()) return;
        root = delete(root, key);
    }
    private Node delete(Node node, Key key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = delete(node.left, key);
        }
        else if (cmp > 0) {
            node.right = delete(node.right, key);
        }
        else {
            if (cache == node) cache = null;
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node cur =  node;
            node = min(cur.right);
            node.right = deleteMin(cur.right);
            node.left = cur.left;
        }
        node.height = 1 + Math.max(height(node.left), height(node.right));
        node.size = size(node.left) + size(node.right) + 1;
        return balance(node);
    }

    public int size() {
        return size(root);
    }

    public boolean isEmpty() {
        return size(root) == 0;
    }

    public static void main(String[] args)
    {
        Set<String> set = new Set<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String key = scanner.next();
            set.add(key);
        }
        for (String s : set.keys())
            System.out.println(s);
        System.out.println("min      : " + set.min());
        System.out.println("max      : " + set.max());
        System.out.println("floor D  : " + set.floor("D"));
        System.out.println("ceiling D: " + set.ceiling("D"));
        System.out.println("rank N   : " + set.rank("N"));
        System.out.println("select 4 : " + set.select(4));
        set.deleteMin();
        set.deleteMax();
        set.delete("E");
        set.delete("M");
        for (String s : set.keys())
            System.out.println(s);
    }
}
