package pers.luo.algs;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * AVL balanced binary search tree implementation of an ordered symbol table
 * @param <Key>
 * @param <Value>
 *
 * @author Luo Wenyang
 */
@SuppressWarnings("unchecked")
public class AVLBST<Key extends Comparable<Key>, Value> implements OrderedST<Key, Value>, java.io.Serializable {
    transient private Node<Key, Value> root;
    transient private Node<Key, Value> cache; // software caching

    //private int treeLevel;

    private static class Node<Key extends Comparable<Key>, Value> implements java.io.Serializable {
        Key key;
        Value value;
        transient Node<Key, Value> left, right;
        int size;
        int height;
        //double xCoordinate, yCoordinate;
        public Node(Key key, Value value, int size, int height) {
            this.key    = key;
            this.value  = value;
            this.left   = null;
            this.right  = null;
            this.size   = size;
            this.height = height;
        }
    }

    // helper to avoid judging null explicitly
    private int size(Node<Key, Value> node) {
        return node == null ? 0 : node.size;
    }

    // helper to avoid judging null explicitly
    private int height(Node<Key, Value> node) {
        return node == null ? 0 : node.height;
    }

    private Node<Key, Value> rotateLeft(Node<Key, Value> node) {
        Node<Key, Value> x = node.right;
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

    private Node<Key, Value> rotateRight(Node<Key, Value> node) {
        Node<Key, Value> x = node.left;
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

    private int balanceFactor(Node<Key, Value> node) {
        if (node == null) return 0; // necessary because child would be called and it could be null
        return height(node.left) - height(node.right);
    }

    private Node<Key, Value> balance(Node<Key, Value> node) {
        if (balanceFactor(node) > 1) {
            if (balanceFactor(node.left) < 0) node.left = rotateLeft(node.left);  // case 2
            node = rotateRight(node);   // case 1
        }
        else if (balanceFactor(node) < -1) {
            if (balanceFactor(node.right) > 0) node.right = rotateRight(node.right);  // case 3
            node = rotateLeft(node);   // case 4
        }
        return node;
    }

    @Override
    public Key min() {
        if (isEmpty()) return null;
        return min(root).key;
    }
    private Node<Key, Value> min(Node<Key, Value> node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;
        return node;
    }

    @Override
    public Key max() {
        if (isEmpty()) return null;
        return max(root).key;
    }
    private Node<Key, Value> max(Node<Key, Value> node) {
        if (node == null) return null;
        while (node.right != null) node = node.right;
        return node;
    }

    @Override
    public Key floor(Key key) {
        Key candidate = null;
        Node<Key, Value> cur = root;
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
        Node<Key, Value> cur = root;
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
        Node<Key, Value> cur = root;
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
        Node<Key, Value> cur = root;
        while (cur != null) {
            int t = size(cur.left);
            if      (t > k) { cur = cur.left;              }
            else if (t < k) { k -= t + 1; cur = cur.right; }
            else            { cache = cur; return cur.key; }
        }
        return null;    // will trigger only if k is out of bound
    }

    @Override
    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new Queue<>();
        keys(root, queue, lo, hi);
        return queue;
    }
    private void keys(Node<Key, Value> node, Queue<Key> queue, Key lo, Key hi) {
        if (node == null) return;   // basic case
        int cmplo = lo.compareTo(node.key);
        int cmphi = hi.compareTo(node.key);
        if (cmplo < 0) keys(node.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(node.key);
        if (cmphi > 0) keys(node.right, queue, lo, hi);
    }

    @Override
    public void put(Key key, Value value) {
        if (cache != null && cache.key.compareTo(key) == 0) {
            cache.value = value;
            return;
        }
        root = put(root, key, value);
    }
    private Node<Key, Value> put(Node<Key, Value> node, Key key, Value value) {
        if (node == null) {
            cache = new Node<>(key, value, 1, 1);
            return cache;
        }
        int cmp = key.compareTo(node.key);
        if      (cmp < 0) node.left  = put(node.left, key, value);
        else if (cmp > 0) node.right = put(node.right, key, value);
        else              node.value = value;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        node.size = 1 + size(node.left) + size(node.right);
        return balance(node);
    }

    @Override
    public Value get(Key key) {
        if (cache != null && cache.key.compareTo(key) == 0) return cache.value;
        Node<Key, Value> cur = root;
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if      (cmp > 0) cur = cur.left;
            else if (cmp < 0) cur = cur.right;
            else { cache = cur; return cur.value; }
        }
        return null;
    }

    @Override
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Minimum does not exist");
        root = deleteMin(root);
    }
    // delete min from subtree rooted by node while maintaining AVL properties
    private Node<Key, Value> deleteMin(Node<Key, Value> node) {
        if (node.left == null) {
            if (cache == node) cache = null;
            return node.right;
        }
        node.left = deleteMin(node.left);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        node.size = size(node.left) + size(node.right) + 1;
        return balance(node);
    }

    @Override
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Maximum does not exist");
        root = deleteMax(root);
    }
    // delete max from subtree rooted by node while maintaining AVL properties
    private Node<Key, Value> deleteMax(Node<Key, Value> node) {
        if (node.right == null){
            if (cache == node) cache = null;
            return node.left;
        }
        node.right = deleteMax(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        node.size = size(node.left) + size(node.right) + 1;
        return balance(node);
    }

    @Override
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("Argument of delete() cannot be null");
        if (isEmpty()) return;
        root = delete(root, key);
    }
    private Node<Key, Value> delete(Node<Key, Value> node, Key key) {
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
            Node<Key, Value> cur =  node;
            node = min(cur.right);
            node.right = deleteMin(cur.right);
            node.left = cur.left;
        }
        node.height = 1 + Math.max(height(node.left), height(node.right));
        node.size = size(node.left) + size(node.right) + 1;
        return balance(node);
    }

    @Override
    public int size() {
        return size(root);
    }


    /**
     * Serialization methods
     */

    private void writeObject(ObjectOutputStream out) throws IOException {
        writeTree(out, root);
    }

    // recursively write tree to output stream in pre-order
    // start with a mark true or false to denote whether a node exist or not
    private void writeTree(ObjectOutputStream out, Node<Key, Value> node) throws IOException {
        if (node == null) {
            out.writeBoolean(false);
            return;
        }
        out.writeBoolean(true);
        out.writeObject(node);
        writeTree(out, node.left);
        writeTree(out, node.right);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        try {
            root = readTree(in);
        }
        catch (EOFException e) {
            System.out.println("Something happened");
        }
    }

    // recursively read tree from input stream in pre-order
    // start with a mark true or false to denote whether a node exist or not
    private Node<Key, Value> readTree(ObjectInputStream in) throws IOException, ClassNotFoundException {
        if (!in.readBoolean()) return null;
        Node<Key, Value> node = (Node<Key, Value>) in.readObject();
        node.left = readTree(in);
        node.right = readTree(in);
        return node;
    }


    public static void main(String[] args)
    {
        AVLBST<String, Integer> st = new AVLBST<>();
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
        // st.draw();
        st.deleteMin();
        st.deleteMax();
        st.delete("E");
        st.delete("M");
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
        //st.draw();
    }


    /*// below is draw method
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
    }*/
}
