package pers.luo.algs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Abstract data structure of interval tree
 *
 * @author Luo Wenyang
 */
public class IntervalTree {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Interval interval;
        double max;
        boolean color;
        Node left, right, parent;
        public Node(Interval interval, double max, boolean color, Node left, Node right, Node parent) {
            this.interval = interval;
            this.max = max;
            this.color = color;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }
    }

    private Node root = null;

    private double max(Node node) {
        return node == null ? 0.0 : node.max;
    }

    private boolean color(Node node) {
        return node == null ? BLACK : node.color;
    }

    private void updateMax(Node node) {
        assert node != null;
        node.max = Math.max(max(node.left), max(node.right));
        node.max = Math.max(node.max, node.interval.high());
    }

    private void rotateLeft(Node node) {
        assert node != null;
        Node x = node.right;
        assert x != null;
        // deal with x.left
        node.right = x.left;
        if (node.right != null) node.right.parent = node;
        // deal with node.parent
        if      (node.parent == null)      root = x;
        else if (node == node.parent.left) node.parent.left = x;
        else                               node.parent.right = x;
        x.parent = node.parent;
        // deal with node
        x.left = node;
        node.parent = x;
        // update max
        x.max = node.max;
        updateMax(node);
    }

    private void rotateRight(Node node) {
        assert node != null;
        Node x = node.left;
        assert x != null;
        // deal with x.right
        node.left = x.right;
        if (node.left != null) node.left.parent = node;
        // deal with node.parent
        if      (node.parent == null)      root = x;
        else if (node == node.parent.left) node.parent.left = x;
        else                               node.parent.right = x;
        x.parent = node.parent;
        // deal with node
        x.right = node;
        node.parent = x;
        // update max
        x.max = node.max;
        updateMax(node);
    }

    public void put(Interval interval) {
        Node pre = null;
        Node cur = root;
        int cmp = 0;
        while (cur != null) {
            pre = cur;
            cmp = interval.compareTo(cur.interval);
            if      (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else              return;
        }
        // create new node
        cur = new Node(interval, interval.high(), RED, null, null, pre);
        if      (pre == null) root = cur;
        else if (cmp < 0    ) pre.left = cur;
        else                  pre.right = cur;
        // fix up
        insertFixUp(cur);
    }

    private void insertFixUp(Node node) {
        Node x = node.parent;
        while (color(x) == RED) {
            updateMax(x);
            updateMax(x.parent);
            if (x == x.parent.left) {   // left cases
                x = x.parent.right;
                if (color(x) == BLACK) {
                    if (node == node.parent.right) {
                        // case 2
                        node = node.parent;
                        rotateLeft(node);
                    }
                    // case 3
                    node = node.parent;
                    node.color = BLACK;
                    x = node.parent;
                    x.color = RED;
                    rotateRight(x);
                    break;
                }
            } else {    // right cases
                x = x.parent.left;
                if (color(x) == BLACK) {
                    if (node == node.parent.left) {
                        // case 2
                        node = node.parent;
                        rotateRight(node);
                    }
                    // case 3
                    node = node.parent;
                    node.color = BLACK;
                    x = node.parent;
                    x.color = RED;
                    rotateLeft(x);
                    break;
                }
            }
            // case 1
            node = node.parent;
            node.color = BLACK;
            x.color = BLACK;
            node = node.parent;
            node.color = RED;
            x = node.parent;
        }
        for (; x != null; x = x.parent) {
            double originalMax = x.max;
            updateMax(x);
            if (x.max == originalMax) break;
        }
        root.color = BLACK;
    }

    public void delete(Interval interval) {
        Node z = root;
        while (z != null) {
            int cmp = interval.compareTo(z.interval);
            if      (cmp < 0) z = z.left;
            else if (cmp > 0) z = z.right;
            else              break;
        }
        if (z == null) return;
        Node y = z;     // y is the node to be removed
        if (z.left != null && z.right != null) {
            // replace z with y
            y = min(z.right);
            z.interval = y.interval;
        }
        Node x = (y.left == null ? y.right : y.left);   // x is the only child of y
        if (x != null) {
            // replace y with x
            x.parent = y.parent;
            if      (y.parent == null)   root = x;
            else if (y == y.parent.left) y.parent.left = x;
            else                         y.parent.right = x;
            if (y.color == BLACK) deleteFixUp(x);
            x = x.parent;
        } else if (y.parent == null) {
            // y is the only node
            root = null;
        } else {
            // use y as phantom
            if (y.color == BLACK) deleteFixUp(y);
            x = y.parent;
            // replace y with null
            if      (y.parent == null)   root = null;
            else if (y == y.parent.left) y.parent.left = null;
            else                         y.parent.right = null;
        }
        for (; x != null; x = x.parent) {
            double originalMax = x.max;
            updateMax(x);
            if (x.max == originalMax) break;
        }
    }

    private void deleteFixUp(Node node) {
        assert node != null;
        while (node != root && node.color == BLACK) {
            Node x = node.parent;
            if (node == x.left) {           // left cases
                x = x.right;                // x may not be null
                if (color(x) == RED) {
                    // case 1, convert to case 2, 3 or 4
                    x.color = BLACK;            // node.parent.color must be black by Property 2
                    node.parent.color = RED;
                    rotateLeft(node.parent);
                    x = node.parent.right;      // update x to point to node's brother, x may not be null
                }
                if (color(x.left) == BLACK && color(x.right) == BLACK) {
                    // case 2, pull black from node and its brother to node's parent
                    x.color = RED;
                    node = node.parent;
                }
                else {
                    // node's brother must has at least one red child
                    if (color(x.right) == BLACK) {
                        // case 3, move red link to right
                        x.color = RED;
                        x.left.color = BLACK;
                        rotateRight(x);
                        x = x.parent;    // cur is node's new brother
                    }
                    // case 4, remove extra black by lending a black link from node's brother
                    node = x.parent;
                    x.color = node.color;
                    node.color = BLACK;
                    x.right.color = BLACK;        // absorb a black link
                    rotateLeft(node);
                    break;
                }
            }
            else {                          // right cases
                x = x.left;                 // x may not be null
                if (color(x) == RED) {
                    // case 1, convert to case 2, 3 or 4
                    x.color = BLACK;            // node.parent.color must be black by Property 2
                    node.parent.color = RED;
                    rotateRight(node.parent);
                    x = node.parent.left;       // update x to point to node's brother, x may not be null
                }
                if (color(x.left) == BLACK && color(x.right) == BLACK) {
                    // case 2, pull black from node and its brother to node's parent
                    x.color = RED;
                    node = node.parent;
                }
                else {
                    // node's brother must has at least one red child
                    if (color(x.left) == BLACK) {
                        // case 3, move red link to right
                        x.color = RED;
                        x.right.color = BLACK;
                        rotateLeft(x);
                        x = x.parent;    // cur is node's new brother
                    }
                    // case 4, remove extra black by lending a black link from node's brother
                    node = x.parent;
                    x.color = node.color;
                    node.color = BLACK;
                    x.left.color = BLACK;        // absorb a black link
                    rotateRight(node);
                    break;
                }
            }
        }
        node.color = BLACK;
    }

    private Node min(Node node) {
        assert node != null;
        while (node.left != null) node = node.left;
        return node;
    }

    public Interval search(Interval interval) {
        Node x = root;
        while (x != null) {
            if (x.interval.overlaps(interval)) return x.interval;
            if (x.left == null || interval.low() > x.left.max) x = x.right;
            else x = x.left;
        }
        return null;
    }

    public Interval searchMin(Interval interval) {
        Node x = root;
        Interval candidate = null;
        while (x != null) {
            if (x.interval.overlaps(interval)) candidate =  x.interval;
            if (x.left == null || interval.low() > x.left.max) x = x.right;
            else x = x.left;
        }
        return candidate;
    }

    public Iterable<Interval> searchAll(Interval interval) {
        Queue<Interval> queue = new Queue<>();
        collectOverlaps(root, interval, queue);
        return queue;
    }

    private void collectOverlaps(Node node, Interval interval, Queue<Interval> queue) {
        if (node == null) return;
        if (node.left != null && interval.low() <= node.left.max)
            collectOverlaps(node.left, interval, queue);
        if (node.interval.overlaps(interval))
            queue.enqueue(node.interval);
        if (node.right != null && interval.high() >= node.interval.low() && interval.low() <= node.right.max)
            collectOverlaps(node.right, interval, queue);
    }

    public void print() {
        print(root);
        System.out.println();
    }

    private void print(Node node) {
        if (node == null) {
            System.out.print("- ");
            return;
        }
        print(node.left);
        System.out.print(node.interval + "(" + node.max + ") ");
        print(node.right);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(args[0]));
        IntervalTree tree = new IntervalTree();
        while (scanner.hasNext()) {
            double low = scanner.nextDouble();
            double high = scanner.nextDouble();
            tree.put(new Interval(low, high));
        }
        tree.print();
        scanner = new Scanner(System.in);
        System.out.println("test delete:");
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            String[] ss = s.split(" ");
            double low = Double.parseDouble(ss[0]);
            double high = Double.parseDouble(ss[1]);
            tree.delete(new Interval(low, high));
            tree.print();
        }
        System.out.println("test searchMin:");
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            String[] ss = s.split(" ");
            double low = Double.parseDouble(ss[0]);
            double high = Double.parseDouble(ss[1]);
            Interval interval = tree.searchMin(new Interval(low, high));
            System.out.println(interval);
        }
        System.out.println("test searchAll:");
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            String[] ss = s.split(" ");
            double low = Double.parseDouble(ss[0]);
            double high = Double.parseDouble(ss[1]);
            for (Interval interval : tree.searchAll(new Interval(low, high)))
                System.out.print(interval + " ");
            System.out.println();
        }
    }
}
