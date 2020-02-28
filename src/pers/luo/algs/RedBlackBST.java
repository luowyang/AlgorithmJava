package pers.luo.algs;

import edu.princeton.cs.algs4.StdDraw;

import java.util.Scanner;

/**
 * Standard red black tree implementation
 */
public class RedBlackBST<Key extends Comparable<Key>, Value> implements OrderedST<Key, Value> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node<Key extends Comparable<Key>, Value> {
        Key key;
        Value value;
        Node<Key, Value> left, right, parent;
        boolean color;
        int size;

        public Node(Key key, Value value, Node<Key, Value> left, Node<Key, Value> right, Node<Key, Value> parent,
                    boolean color, int size) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            this.parent = parent;
            this.color = color;
            this.size = size;
        }
    }

    private Node<Key, Value> nil = new Node<>(null, null, null, null, null, BLACK, 0);
    private Node<Key, Value> root = nil;

    private Node<Key, Value> cache = nil;

    public int size() {
        return root.size;
    }

    /**
     * rotate left
     * @param node root of the subtree to be rotated
     */
    private void rotateLeft(Node<Key, Value> node) {
        Node<Key, Value> cur = node.right;
        // deal with cur.left
        node.right = cur.left;
        cur.left.parent = node;
        // deal with node.parent
        if      (node == root)             root = cur;
        else if (node == node.parent.left) node.parent.left = cur;
        else                               node.parent.right = cur;
        cur.parent = node.parent;
        // put node to be cur's left child
        cur.left = node;
        node.parent = cur;
        // update size
        cur.size = node.size;
        node.size = node.left.size + node.right.size + 1;
    }

    /**
     * rotate right
     * @param node root of the subtree to be rotated
     */
    private void rotateRight(Node<Key, Value> node) {
        Node<Key, Value> cur = node.left;
        // deal with cur.right
        node.left = cur.right;
        cur.right.parent = node;
        // deal with node.parent
        if      (node == root)             root = cur;
        else if (node == node.parent.left) node.parent.left = cur;
        else                               node.parent.right = cur;
        cur.parent = node.parent;
        // put node to be cur's right child
        cur.right = node;
        node.parent = cur;
        // update size
        cur.size = node.size;
        node.size = node.left.size + node.right.size + 1;
    }

    /**
     * get the value associated with key
     * @param key key
     * @return value, null if key is absent
     */
    public Value get(Key key) {
        if (cache != nil && key.compareTo(cache.key) == 0) return cache.value;
        Node<Key, Value> cur = root;
        while (cur != nil) {
            int cmp = key.compareTo(cur.key);
            if      (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else              return cur.value;
        }
        return null;
    }

    /**
     * fix red black tree from bottom up in insertion
     * @param node root of the subtree to be fixed
     */
    private void insertFixUp(Node<Key, Value> node) {
        while (node.parent.color == RED) {
            Node<Key, Value> cur = node.parent;
            cur.size++;
            cur.parent.size++;
            if (cur == cur.parent.left) {
                cur = cur.parent.right;     // note that cur may be nil
                if (cur.color == RED) {     // case 1
                    node.parent.color = BLACK;
                    cur.color = BLACK;
                    cur.parent.color = RED;
                    node = cur.parent;
                }
                else {
                    if (node == node.parent.right) {
                        // case 2
                        node = node.parent;
                        rotateLeft(node);
                    }
                    // case 3
                    node = node.parent;
                    node.color = BLACK;     // maintain that node has been incremented in size
                    cur = node.parent;
                    cur.color = RED;
                    rotateRight(cur);
                    break;
                }
            }
            else {
                cur = cur.parent.left;      // note that cur may be nil
                if (cur.color == RED) {     // case 1
                    node.parent.color = BLACK;
                    cur.color = BLACK;
                    cur.parent.color = RED;
                    node = cur.parent;
                }
                else {
                    if (node == node.parent.left) {
                        // case 2
                        node = node.parent;
                        rotateRight(node);
                    }
                    // case 3
                    node = node.parent;
                    node.color = BLACK;     // maintain that node has been incremented in size
                    cur = node.parent;
                    cur.color = RED;
                    rotateLeft(cur);
                    break;
                }
            }
        }
        // when the while loop ends, node has already been incremented
        for (node = node.parent; node != nil; node = node.parent)
            node.size++;
        root.color = BLACK;
    }

    /**
     * put a key-value pair
     * @param key key
     * @param value value associated with the key
     */
    public void put(Key key, Value value) {
        if (cache != nil && key.compareTo(cache.key) == 0) {
            cache.value = value;
            return;
        }
        Node<Key, Value> cur = root;
        Node<Key, Value> pre = nil;
        // find the place to put into
        int cmp = 0;
        while (cur != nil) {
            pre = cur;
            cmp = key.compareTo(cur.key);
            if      (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else              { cur.value = value; return; }
        }
        // create a new red node and put it to the right place
        cur = new Node<>(key, value, nil, nil, pre, RED, 1);
        if      (pre == nil) root = cur;        // root does not exist
        else if (cmp < 0   ) pre.left = cur;
        else                 pre.right = cur;
        // fix up
        insertFixUp(cur);
        // update cache
        cache = cur;
    }

    /**
     * fix red black tree from bottom up in deletion
     * @param node root of the subtree to be fixed
     */
    private void deleteFixUp(Node<Key, Value> node) {
        while (node != root && node.color == BLACK) {
            Node<Key, Value> cur = node.parent;
            cur.size--;
            if (node == cur.left) {         // left cases
                cur = cur.right;            // cur may be nil
                if (cur.color == RED) {
                    // case 1, convert to case 2, 3 or 4
                    cur.color = BLACK;      // node.parent.color must be black by Property 2
                    node.parent.color = RED;
                    rotateLeft(node.parent);
                    cur = node.parent.right;    // update cur to point to node's brother
                }
                if (cur.left.color == BLACK && cur.right.color == BLACK) {
                    // case 2, pull black from node and its brother to node's parent
                    cur.color = RED;
                    node = node.parent;
                }
                else {
                    // node's brother must has at least one red child
                    if (cur.right.color == BLACK) {
                        // case 3, move red link to right
                        cur.color = RED;
                        cur.left.color = BLACK;
                        rotateRight(cur);
                        cur = node.parent.right;    // cur is node's new brother
                    }
                    // case 4, remove extra black by lending a black link from node's brother
                    node = node.parent;
                    cur.color = node.color;
                    node.color = BLACK;
                    cur.right.color = BLACK;        // absorb a black link
                    rotateLeft(node);
                    break;
                }
            }
            else {                          // right cases
                cur = cur.left;             // cur may be nil
                if (cur.color == RED) {
                    // case 1, convert to case 2, 3 or 4
                    cur.color = BLACK;      // node.parent.color must be black by Property 2
                    node.parent.color = RED;
                    rotateRight(node.parent);
                    cur = node.parent.left;     // update cur to point to node's brother
                }
                if (cur.left.color == BLACK && cur.right.color == BLACK) {
                    // case 2, pull black from node and its brother to node's parent
                    cur.color = RED;
                    node = node.parent;
                }
                else {
                    // node's brother must has at least one red child
                    if (cur.left.color == BLACK) {
                        // case 3, move red link to left
                        cur.color = RED;
                        cur.right.color = BLACK;
                        rotateLeft(cur);
                        cur = node.parent.left;    // cur is node's new brother
                    }
                    // case 4, remove extra black by lending a black link from node's brother
                    node = node.parent;
                    cur.color = node.color;
                    node.color = BLACK;
                    cur.left.color = BLACK;         // absorb a black link
                    rotateRight(node);
                    break;
                }
            }
        }
        // absorb extra black
        node.color = BLACK;
        // node has already been incremented when while loop ends
        for (node = node.parent; node != nil; node = node.parent)
            node.size--;
    }

    /**
     * transplant source subtree to target node
     * not maintaining child links
     * @param source source subtree, may be nil
     * @param target target node
     */
    private void transplant(Node<Key, Value> source, Node<Key, Value> target) {
        if (target.parent == nil) {
            root = source;
        }
        else if (target == target.parent.left) {
            target.parent.left = source;
        }
        else {
            target.parent.right = source;
        }
        // even if source is nil, we will link nil to target's parent anyway
        // this will pay off when we try to fix up
        source.parent = target.parent;
    }

    /**
     * delete a key-value pair
     * ignore absent keys
     * @param key key to be deleted
     */
    public void delete(Key key) {
        Node<Key, Value> cur = root;
        while (cur != nil) {
            int cmp = key.compareTo(cur.key);
            if      (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else              break;    // hit
        }
        if (cur == nil) return;         // ignore absent keys
        Node<Key, Value> x, y = cur;
        boolean yColor = y.color;
        if (cur.left == nil) {
            x = cur.right;
            transplant(x, cur);
        }
        else if (cur.right == nil) {
            x = cur.left;
            transplant(x, cur);
        }
        else {
            y = min(cur.right);
            yColor = y.color;
            x = y.right;
            if (y.parent == cur)    // we don't want to update x's parent if y is cur's child
                x.parent = y;       // x can be nil
            else {
                // x goes into y's position
                transplant(x, y);
                // update right link
                y.right = cur.right;
                y.right.parent = y;
            }
            transplant(y, cur);     // y goes int cur's position
            // update left link
            y.left = cur.left;
            y.left.parent = y;      // y.left is not nil
            // update color
            y.color = cur.color;
            // update size
            y.size = cur.size;
        }
        if (yColor == BLACK)
            deleteFixUp(x);
        else    // only update size
            for (x = x.parent; x != nil; x = x.parent)
                x.size--;
        if (cache != nil && key.compareTo(cache.key) == 0)
            cache = nil;
    }

    @Override
    public boolean isEmpty() {
        return root.size == 0;
    }

    /**
     * find the minimum node in node's subtree
     * @param node root of the subtree
     * @return the minimum node in the subtree
     */
    private Node<Key, Value> min(Node<Key, Value> node) {
        if (node == nil) return nil;
        while (node.left != nil) node = node.left;
        return node;
    }

    /**
     * find the maximum node in node's subtree
     * @param node root of the subtree
     * @return the maximum node in the subtree
     */
    private Node<Key, Value> max(Node<Key, Value> node) {
        if (node == nil) return nil;
        while (node.right != nil) node = node.right;
        return node;
    }

    @Override
    public Key min() {
        return min(root).key;
    }

    @Override
    public Key max() {
        return max(root).key;
    }

    @Override
    public Key floor(Key key) {
        Node<Key, Value> cur = root;
        Node<Key, Value> candidate = nil;
        while (cur != nil) {
            int cmp = key.compareTo(cur.key);
            if      (cmp < 0) cur = cur.left;
            else if (cmp > 0) { candidate = cur; cur = cur.right; }
            else              return cur.key;
        }
        return candidate.key;
    }

    @Override
    public Key ceiling(Key key) {
        Node<Key, Value> cur = root;
        Node<Key, Value> candidate = nil;
        while (cur != nil) {
            int cmp = key.compareTo(cur.key);
            if      (cmp > 0) cur = cur.right;
            else if (cmp < 0) { candidate = cur; cur = cur.left; }
            else              return cur.key;
        }
        return candidate.key;
    }

    @Override
    public int rank(Key key) {
        Node<Key, Value> cur = root;
        int r = 0;
        while (cur != nil) {
            int cmp = key.compareTo(cur.key);
            if      (cmp < 0) cur = cur.left;
            else if (cmp > 0) { r += cur.left.size + 1; cur = cur.right; }
            else              { r += cur.left.size; break; }
        }
        return r;
    }

    @Override
    public Key select(int k) {
        Node<Key, Value> cur = root;
        while (cur != nil) {
            int sz = cur.left.size;
            if      (k < sz) cur = cur.left;
            else if (k > sz) { k -= sz + 1; cur = cur.right; }
            else             { return cur.key; }
        }
        return null;
    }

    @Override
    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new Queue<>();
        collect(root, lo, hi, queue);
        return queue;
    }

    private void collect(Node<Key, Value> node, Key lo, Key hi, Queue<Key> queue) {
        if (node == nil) return;
        int cmplo = lo.compareTo(node.key);
        int cmphi = hi.compareTo(node.key);
        if (cmplo < 0) collect(node.left, lo, hi, queue);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(node.key);
        if (cmphi > 0) collect(node.right, lo, hi, queue);
    }

    public static void main(String[] args)
    {
        RedBlackBST<String, Integer> st = new RedBlackBST<>();
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
        st.delete("E");
        st.delete("M");
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
    }
}
