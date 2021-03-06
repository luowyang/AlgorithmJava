package pers.luo.algs;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class BST<Key extends Comparable<Key>, Value> implements OrderedST<Key, Value> {
    private Node root;
    private Node cache;

    private int treeLevel;

    private class Node {
        Key key;
        Value value;
        Node left;
        Node right;
        int size;
        double xCoordinate, yCoordinate;
        public Node(Key key, Value value, int size) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.size = size;
        }
    }

    @Override
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("Minimum does not exist");
        return min(root).key;
    }
    private Node min(Node node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;
        return node;
    }

    @Override
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("Maximum does not exist");
        return max(root).key;
    }
    private Node max(Node node) {
        if (node == null) return null;
        while (node.right != null) node = node.right;
        return node;
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
        int r = 0;          // r stores # of known nodes less than key
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if      (cmp > 0) { cur = cur.left;                           }
            else if (cmp < 0) { r += size(cur.left) + 1; cur = cur.right; }
            else              { cache = cur; return r + size(cur.left);   }
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
            else            { cache = cur; return cur.key; }
        }
        return null;    // will trigger only if k is out of bound
    }

    @Override
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Minimum does not exist");
        root = deleteMin(root);
    }
    private Node deleteMin(Node node) {
        if (node.left == null) {
            if (cache == node) cache = null;
            return node.right;
        }
        node.left = deleteMin(node.left);
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    @Override
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Maximum does not exist");
        root = deleteMax(root);
    }
    private Node deleteMax(Node node) {
        if (node.right == null) {
            if (cache == node) cache = null;
            return node.left;
        }
        node.right = deleteMin(node.right);
        node.size = size(node.left) + size(node.right) + 1;
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
            if (cache == node) cache = null;
            if (node.left == null ) return node.right;
            if (node.right == null) return node.left;
            // randomly delete using predecessor or successor
            Node cur = node;
            if (StdRandom.uniform(2) == 0) {
                node = min(cur.right);
                node.right = deleteMin(cur.right);
                node.left = cur.left;
            }
            else {
                node = max(cur.left);
                node.left = deleteMax(cur.left);
                node.right = cur.right;
            }
        }
        node.size = size(node.left) + size(node.right) + 1;
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
        if (cache != null && cache.key.compareTo(key) == 0) {
            cache.value = value;
            return;
        }
        root = put(root, key, value);
    }
    private Node put(Node node, Key key, Value value)
    {   // recursively put key-value pair into sub-tree rooted by "node"
        // return the root of the sub-tree
        if (node == null) {
            cache = new Node(key, value, 1);
            return cache;
        }
        int cmp = node.key.compareTo(key);
        if      (cmp > 0) { node.left = put(node.left, key, value);        }
        else if (cmp < 0) { node.right = put(node.right, key, value);      }
        else              { cache = node; node.value = value; return node; }
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    @Override
    public Value get(Key key) {
        if (cache != null && cache.key.compareTo(key) == 0) return cache.value;
        Node cur = root;
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if      (cmp > 0) cur = cur.left;
            else if (cmp < 0) cur = cur.right;
            else { cache = cur; return cur.value; }
        }
        return null;
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        return node == null ? 0 : node.size;
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
//        st.draw();
        st.deleteMin();
        st.deleteMax();
//        st.draw();
        st.delete("E");
        st.delete("M");
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
        st.draw();
    }


    // below is draw method
    public void draw() {
        treeLevel = 0;
        setCoordinates(root, 0.9);

        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        drawLines(root);
        drawNodes(root);
    }

    private void setCoordinates(Node node, double distance) {
        if (node == null) {
            return;
        }

        setCoordinates(node.left, distance - 0.05);
        node.xCoordinate = (0.5 + treeLevel++) / size();
        node.yCoordinate = distance - 0.05;
        setCoordinates(node.right, distance - 0.05);
    }

    private void drawLines(Node node) {
        if (node == null) {
            return;
        }

        drawLines(node.left);

        if (node.left != null) {
            StdDraw.line(node.xCoordinate, node.yCoordinate, node.left.xCoordinate, node.left.yCoordinate);
            resetPen();
        }
        if (node.right != null) {
            StdDraw.line(node.xCoordinate, node.yCoordinate, node.right.xCoordinate, node.right.yCoordinate);
            resetPen();
        }

        drawLines(node.right);
    }

    private void resetPen() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.0025);
    }

    private void drawNodes(Node node) {
        if (node == null) {
            return;
        }

        double nodeRadius = 0.032;

        drawNodes(node.left);

        StdDraw.setPenColor(StdDraw.WHITE);
        //Clear the node circle area
        StdDraw.filledCircle(node.xCoordinate, node.yCoordinate, nodeRadius);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.circle(node.xCoordinate, node.yCoordinate, nodeRadius);
        StdDraw.text(node.xCoordinate, node.yCoordinate, String.valueOf(node.key));

        drawNodes(node.right);
    }
}
