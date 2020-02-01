package pers.luo.algs;

import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdDraw;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class LLRedBlackBST<Key extends Comparable<Key>, Value> implements OrderedST<Key, Value> {
    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;
    private Node cache;

    private int treeLevel;

    private class Node {
        Key key;            // key
        Value value;        // value
        Node left, right;   // left and right subtree
        int size;           // node counter
        boolean color;      // color of the node and the link pointed to it
        double xCoordinate, yCoordinate;
        public Node(Key key, Value value, int size, boolean color) {
            this.key   = key;
            this.value = value;
            this.size = size;
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
        return node.size;
    }

    // left rotation will rotate right child to the root
    // the link from "node" to its right child is RED
    private Node rotateLeft(Node node) {
        Node x = node.right;    // x denotes right child of current root "node"
        node.right = x.left;    // set node.right to point to x.left, so as to free x.left
        x.left = node;          // now we can set x.left to point to node, and x becomes new root
        x.color = node.color;   // x is now pointed by the original parent of node, so set x's color to be node's
        node.color = RED;       // the original link from node to x is RED, so after rotation node is pointed by RED link
        x.size = node.size;           // x is the current root so its counter should be the same as the original root "node"
        node.size = size(node.left) + size(node.right) + 1;    // update node's counter with its children's counter
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
        x.size = node.size;           // x is the current root so its counter should be the same as the original root "node"
        node.size = size(node.left) + size(node.right) + 1;    // update node's counter with its children's counter
        return x;               // return the link to the current root x
    }

    // flip colors of node and its children
    // node must have the opposite color of its children
    // while its two children must have the same color
    private void flipColors(Node node) {
        node.color = !node.color;               // reverse node color
        node.left.color = !node.left.color;     // reverse left child color
        node.right.color = !node.right.color;   // reverse right child color
    }

    // if left child is a 2-node, a red link should be borrowed from parent or brother
    // node must be red
    private Node moveRedLeft(Node node) {
        flipColors(node);   // whether right child is red or not, we shall flip color first
        if (isRed(node.right.left)) {   // if right child is null, left child must be red
            node.right = rotateRight(node.right);   // re-balance 5-node
            node = rotateLeft(node);                // adjust key layout inside 5-node
            flipColors(node);                       // divide 5-node
        }
        return node;
    }

    // if right child is a 2-node, a red link should be borrowed from parent or brother
    // node must be red
    private Node moveRedRight(Node node) {
        flipColors(node);   // whether right child is red or not, we shall flip color first
        if (isRed(node.left.left)) {
            node = rotateLeft(node);                // adjust key layout inside 5-node
            flipColors(node);                       // divide 5-node
        }
        return node;
    }

    // balance red black tree after deletion
    private Node balance(Node node) {
        if (isRed(node.right))                         node = rotateLeft(node);     // repair right leaning RED link
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);    // re-balance left skewed 4-node
        if (isRed(node.left) && isRed(node.right))     flipColors(node);            // decompose balanced 4-node
        node.size = size(node.left) + size(node.right) + 1;    // set node counter
        return node;    // return link to current root
    }

    @Override
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Minimum does not exist");
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;   // set the invariant
        root = deleteMin(root);
        if (!isEmpty()) root.color = BLACK; // reset root color to black
    }
    private Node deleteMin(Node node) {
        if (node.left == null) return null; // delete if reach min
        if (!isRed(node.left) && !isRed(node.left.left))    // if left child is a 2-node
            node = moveRedLeft(node);   // change left child into non-2-node
        node.left = deleteMin(node.left);
        return balance(node);
    }

    @Override
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Maximum does not exist");
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;   // set the invariant
        root = deleteMax(root);
        if (!isEmpty()) root.color = BLACK; // reset root color to black
    }
    private Node deleteMax(Node node) {
        if (isRed(node.left)) node = rotateRight(node); // rotate red left link to right
        if (node.right == null) return null; // delete if reach max
        if (!isRed(node.right) && !isRed(node.right.left))    // if right child is a 2-node
            node = moveRedRight(node);   // change right child into non-2-node
        node.right = deleteMax(node.right);
        return balance(node);
    }

    @Override
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(key)) return;
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;
        root = delete(root, key);
        if (!isEmpty()) root.color = BLACK;
    }
    private Node delete(Node node, Key key) {
        if (key.compareTo(node.key) < 0)  {
            if (!isRed(node.left) && !isRed(node.left.left))
                node = moveRedLeft(node);
            node.left = delete(node.left, key);
        }
        else {
            if (isRed(node.left))
                node = rotateRight(node);
            if (key.compareTo(node.key) == 0 && (node.right == null))
                return null;
            if (!isRed(node.right) && !isRed(node.right.left))
                node = moveRedRight(node);
            if (key.compareTo(node.key) == 0) {
                Node x = min(node.right);
                node.key = x.key;
                node.value = x.value;
                node.right = deleteMin(node.right);
            }
            else node.right = delete(node.right, key);
        }
        return balance(node);
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
            else              { cache = cur; return r + size(cur.left);   }
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
            else            { cache = cur; return cur.key; }
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
        if (cache != null && cache.key.compareTo(key) == 0) {
            cache.value = value;
            return;
        }
        root = put(root, key, value);   // recursive insert on root, return new root
        root.color = BLACK;             // root color is always BLACK (root is pointed by null link)
    }
    // recursively insert new node and adjust parents
    // return link to the root after insertion
    private Node put(Node node, Key key, Value value) {
        if (node == null) {
            cache = new Node(key, value, 1, RED);
            return cache;  // new node is inserted if miss and is always RED
        }
        int cmp = key.compareTo(node.key);  // compare searching key to current node's key
        if      (cmp < 0) node.left = put(node.left, key, value);   // if less, turn to left subtree
        else if (cmp > 0) node.right = put(node.right, key, value); // if more, turn to right subtree
        else              { cache = node; node.value = value; }     // if hit, change current value
        // now we should check red black properties
        if (!isRed(node.left) && isRed(node.right))    node = rotateLeft(node);     // repair right leaning RED link
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);    // re-balance left skewed 4-node
        if (isRed(node.left) && isRed(node.right))     flipColors(node);            // decompose balanced 4-node

        node.size = size(node.left) + size(node.right) + 1;    // set node counter
        return node;    // return link to current root
    }

    @Override
    public Value get(Key key) {
        if (cache != null && cache.key.compareTo(key) == 0) return cache.value;
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);   // compare search key to current node's key
            if      (cmp < 0) cur = cur.left;   // if less, turn to left subtree
            else if (cmp > 0) cur = cur.right;  // if more, turn to right subtree
            else { cache = cur; return cur.value; }// if hit, return search result
        }
        return null;    // if miss, return null
    }

    public static void main(String[] args) {
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
        st.deleteMin();
        st.deleteMax();
        st.delete("L");
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
            if (node.left.color == RED) {
                setPenToWriteRedLine();
            }

            StdDraw.line(node.xCoordinate, node.yCoordinate, node.left.xCoordinate, node.left.yCoordinate);
            resetPen();
        }
        if (node.right != null) {
            if (node.right.color == RED) {
                setPenToWriteRedLine();
            }

            StdDraw.line(node.xCoordinate, node.yCoordinate, node.right.xCoordinate, node.right.yCoordinate);
            resetPen();
        }

        drawLines(node.right);
    }

    private void setPenToWriteRedLine() {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.007);
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

        StdDraw.setPenColor(node.color?StdDraw.RED:StdDraw.BLACK);
        StdDraw.circle(node.xCoordinate, node.yCoordinate, nodeRadius);
        StdDraw.text(node.xCoordinate, node.yCoordinate, String.valueOf(node.key));

        drawNodes(node.right);
    }
}
