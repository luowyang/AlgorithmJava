package pers.luo.algs;

import java.util.Scanner;

/**
 * Standard red black tree implementation
 */
public class RedBlackBST<K extends Comparable<K>, V> implements OrderedST<K, V> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node<K extends Comparable<K>, V> {
        K key;
        V value;
        Node<K, V> left, right, parent;
        boolean color;
        int size;

        public Node(K key, V value, Node<K, V> left, Node<K, V> right, Node<K, V> parent,
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

    private Node<K, V> nil = new Node<>(null, null, null, null, null, BLACK, 0);
    private Node<K, V> root = nil;

    private Node<K, V> cache = nil;

    public int size() {
        return root.size;
    }

    /**
     * rotate left
     * @param node root of the subtree to be rotated
     */
    private void rotateLeft(Node<K, V> node) {
        Node<K, V> cur = node.right;
        // deal with cur.left
        node.right = cur.left;
        if (cur.left != nil) cur.left.parent = node;
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
    private void rotateRight(Node<K, V> node) {
        Node<K, V> cur = node.left;
        // deal with cur.right
        node.left = cur.right;
        if (cur.right != nil) cur.right.parent = node;
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
    public V get(K key) {
        if (cache != nil && key.compareTo(cache.key) == 0) return cache.value;
        Node<K, V> cur = root;
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
    private void insertFixUp(Node<K, V> node) {
        while (node.parent.color == RED) {
            Node<K, V> cur = node.parent;
            cur.size++;
            cur.parent.size++;
            if (cur == cur.parent.left) {
                cur = cur.parent.right;     // note that cur may be nil
                if (cur.color == BLACK) {
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
                if (cur.color == BLACK) {
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
            if (cur.color == RED) {     // case 1
                node.parent.color = BLACK;
                cur.color = BLACK;
                cur.parent.color = RED;
                node = cur.parent;
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
    public void put(K key, V value) {
        if (cache != nil && key.compareTo(cache.key) == 0) {
            cache.value = value;
            return;
        }
        Node<K, V> cur = root;
        Node<K, V> pre = nil;
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
    private void deleteFixUp(Node<K, V> node) {
        while (node != root && node.color == BLACK) {
            Node<K, V> cur = node.parent;
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
                        cur = cur.parent;    // cur is node's new brother
                    }
                    // case 4, remove extra black by lending a black link from node's brother
                    node = cur.parent;
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
                        cur = cur.parent;    // cur is node's new brother
                    }
                    // case 4, remove extra black by lending a black link from node's brother
                    node = cur.parent;
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
    }

    /**
     * delete a key-value pair
     * ignore absent keys
     * @param key key to be deleted
     */
    public void delete(K key) {
        Node<K, V> z = root;
        while (z != nil) {
            int cmp = key.compareTo(z.key);
            if      (cmp < 0) z = z.left;
            else if (cmp > 0) z = z.right;
            else              break;    // hit
        }
        if (z == nil) return;         // ignore absent keys
        Node<K, V> y = z;
        if (z.left != nil && z.right != nil) {
            y = min(z.right);
            z.key = y.key;
            z.value = y.value;
        }
        Node<K, V> x = (y.left == nil ? y.right : y.left);
        if      (y.parent == nil)    root = x;
        else if (y == y.parent.left) y.parent.left = x;
        else                         y.parent.right = x;
        x.parent = y.parent;
        if (y.color == BLACK) deleteFixUp(x);
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
    private Node<K, V> min(Node<K, V> node) {
        if (node == nil) return nil;
        while (node.left != nil) node = node.left;
        return node;
    }

    /**
     * find the maximum node in node's subtree
     * @param node root of the subtree
     * @return the maximum node in the subtree
     */
    private Node<K, V> max(Node<K, V> node) {
        if (node == nil) return nil;
        while (node.right != nil) node = node.right;
        return node;
    }

    @Override
    public K min() {
        return min(root).key;
    }

    @Override
    public K max() {
        return max(root).key;
    }

    @Override
    public K floor(K key) {
        Node<K, V> cur = root;
        Node<K, V> candidate = nil;
        while (cur != nil) {
            int cmp = key.compareTo(cur.key);
            if      (cmp < 0) cur = cur.left;
            else if (cmp > 0) { candidate = cur; cur = cur.right; }
            else              return cur.key;
        }
        return candidate.key;
    }

    @Override
    public K ceiling(K key) {
        Node<K, V> cur = root;
        Node<K, V> candidate = nil;
        while (cur != nil) {
            int cmp = key.compareTo(cur.key);
            if      (cmp > 0) cur = cur.right;
            else if (cmp < 0) { candidate = cur; cur = cur.left; }
            else              return cur.key;
        }
        return candidate.key;
    }

    @Override
    public int rank(K key) {
        Node<K, V> cur = root;
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
    public K select(int k) {
        Node<K, V> cur = root;
        while (cur != nil) {
            int sz = cur.left.size;
            if      (k < sz) cur = cur.left;
            else if (k > sz) { k -= sz + 1; cur = cur.right; }
            else             { return cur.key; }
        }
        return null;
    }

    @Override
    public Iterable<K> keys(K lo, K hi) {
        Queue<K> queue = new Queue<>();
        collect(root, lo, hi, queue);
        return queue;
    }

    private void collect(Node<K, V> node, K lo, K hi, Queue<K> queue) {
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
