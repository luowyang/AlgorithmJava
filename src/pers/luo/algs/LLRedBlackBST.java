package pers.luo.algs;

public class LLRedBlackBST<Key extends Comparable<Key>, Value> implements OrderedST<Key, Value> {
    private static final boolean RED   = true;
    private static final boolean BLACK = false;

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
        return null;
    }

    @Override
    public Key max() {
        return null;
    }

    @Override
    public Key floor(Key key) {
        return null;
    }

    @Override
    public Key ceiling(Key key) {
        return null;
    }

    @Override
    public int rank(Key key) {
        return 0;
    }

    @Override
    public Key select(int k) {
        return null;
    }

    @Override
    public int size(Key lo, Key hi) {
        return 0;
    }

    @Override
    public Iterable<Key> keys(Key lo, Key hi) {
        return null;
    }

    @Override
    public void put(Key key, Value value) {

    }

    @Override
    public Value get(Key key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }



}
