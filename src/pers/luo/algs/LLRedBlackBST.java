package pers.luo.algs;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class LLRedBlackBST<Key extends Comparable<Key>, Value> implements OrderedST<Key, Value> {
    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;

    private class Node {
        Key key;            // key
        Value value;        // value
        Node left, right;   // left and right subtree
        int N;              // node counter
        boolean color;      // color of the node and the link pointed to it
        public Node(Key key, Value value, int N, boolean color) {
            this.key   = key;
            this.value = value;
            this.N     = N;
            this.color = color;
        }
    }

    // helper to determine if a node is red
    private boolean isRed(Node node) {
        if (node == null) return false; // null node (null link) is always black
        return node.color == RED;
    }

    // size of the subtree rooted by "node"
    private int size(Node node) {
        if (node == null) return 0;
        return node.N;
    }

    // left rotation will rotate right child to the root
    // the link from "node" to its right child is RED
    private Node rotateLeft(Node node) {
        Node x = node.right;    // x denotes right child of current root "node"
        node.right = x.left;    // set node.right to point to x.left, so as to free x.left
        x.left = node;          // now we can set x.left to point to node, and x becomes new root
        x.color = node.color;   // x is now pointed by the original parent of node, so set x's color to be node's
        node.color = RED;       // the original link from node to x is RED, so after rotation node is pointed by RED link
        x.N = node.N;           // x is the current root so its counter should be the same as the original root "node"
        node.N = size(node.left) + size(node.right) + 1;    // update node's counter with its children's counter
        return x;               // return the link to the current root x
    }

    // right rotation will rotate left child to the root
    // the link from "node" to its left child is RED
    private Node rotateRight(Node node) {
        Node x = node.left;     // x denotes right child of current root "node"
        node.left = x.right;    // set node.right to point to x.left, so as to free x.left
        x.right = node;         // now we can set x.left to point to node, and x becomes new root
        x.color = node.color;   // x is now pointed by the original parent of node, so set x's color to be node's
        node.color = RED;       // the original link from node to x is RED, so after rotation node is pointed by RED link
        x.N = node.N;           // x is the current root so its counter should be the same as the original root "node"
        node.N = size(node.left) + size(node.right) + 1;    // update node's counter with its children's counter
        return x;               // return the link to the current root x
    }

    // flip colors of children from red to black, both children should be red
    // also flip "node" to red to maintain black height, normally "node" is black
    private void flipColors(Node node) {
        node.color = RED;           // set node color to RED
        node.left.color = BLACK;    // set left child color to BLACK
        node.right.color = BLACK;   // set right child color to BLACK
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
            int cmp = key.compareTo(cur.key);
            if      (cmp < 0) { cur = cur.left;                       }
            else if (cmp > 0) { candidate = cur.key; cur = cur.right; }
            else              { return cur.key;                       }
        }
        return candidate;
    }

    @Override
    public Key ceiling(Key key) {
        Key candidate = null;
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if      (cmp > 0) { cur = cur.right;                     }
            else if (cmp < 0) { candidate = cur.key; cur = cur.left; }
            else              { return cur.key;                      }
        }
        return candidate;
    }

    @Override
    public int rank(Key key) {
        Node cur = root;
        int r = 0;          // r stores # of known nodes less than key
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if      (cmp < 0) { cur = cur.left;                           }
            else if (cmp > 0) { r += size(cur.left) + 1; cur = cur.right; }
            else              { return r + size(cur.left); }
        }
        return r;
    }

    @Override
    public Key select(int k) {
        Node cur = root;
        while (cur != null) {
            int t = size(cur.left); // get left subtree size
            if      (t > k) { cur = cur.left;              }
            else if (t < k) { k -= t + 1; cur = cur.right; }
            else            { return cur.key;              }
        }
        return null;    // will trigger only if k is out of bound
    }

    @Override
    public int size() {
        return size(root);
    }

    @Override
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

    @Override
    public void put(Key key, Value value) {
        root = put(root, key, value);   // recursive insert on root, return new root
        root.color = BLACK;             // root color is always BLACK (root is pointed by null link)
    }
    // recursively insert new node and adjust parents
    // return link to the root after insertion
    private Node put(Node node, Key key, Value value) {
        if (node == null) return new Node(key, value, 1, RED);  // new node is inserted if miss and is always RED
        int cmp = key.compareTo(node.key);  // compare searching key to current node's key
        if      (cmp < 0) node.left = put(node.left, key, value);   // if less, turn to left subtree
        else if (cmp > 0) node.right = put(node.right, key, value); // if more, turn to right subtree
        else              node.value = value;                       // if hit, change current value
        // now we should check red black properties
        if (!isRed(node.left) && isRed(node.right))    node = rotateLeft(node);     // repair right leaning RED link
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);    // repair left skewed 4-node
        if (isRed(node.left) && isRed(node.right))     flipColors(node);            // decompose balanced 4-node

        node.N = size(node.left) + size(node.right) + 1;    // set node counter
        return node;    // return link to current root
    }

    @Override
    public Value get(Key key) {
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);   // compare search key to current node's key
            if      (cmp < 0) cur = cur.left;   // if less, turn to left subtree
            else if (cmp > 0) cur = cur.right;  // if more, turn to right subtree
            else              return cur.value; // if hit, return search result
        }
        return null;    // if miss, return null
    }

    public static void main(String[] args)
    {
        LLRedBlackBST<String, Integer> st = new LLRedBlackBST<>();
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
        /*st.deleteMin();
        st.deleteMax();
        st.delete("L");
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));*/
    }
}
