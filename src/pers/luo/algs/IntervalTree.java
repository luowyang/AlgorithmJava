package pers.luo.algs;

import edu.princeton.cs.algs4.In;

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

    private final Node nil = new Node(null, Double.MIN_VALUE, BLACK, null, null, null);
    private Node root = nil;

    private void updateMax(Node node) {
        node.max = Math.max(node.left.max, node.right.max);
        node.max = Math.max(node.max, node.interval.high());
    }

    private void rotateLeft(Node node) {
        Node x = node.right;
        // deal with x.left
        node.right = x.left;
        node.right.parent = node;
        // deal with node.parent
        if      (node.parent == nil)       root = x;
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
        Node x = node.left;
        // deal with x.right
        node.left = x.right;
        node.left.parent = node;
        // deal with node.parent
        if      (node.parent == nil)       root = x;
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
        Node pre = nil;
        Node cur = root;
        int cmp = 0;
        while (cur != nil) {
            pre = cur;
            cmp = interval.compareTo(cur.interval);
            if      (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else              return;
        }
        // create new node
        cur = new Node(interval, interval.high(), RED, nil, nil, pre);
        if      (pre == nil) root = cur;
        else if (cmp < 0   ) pre.left = cur;
        else                 pre.right = cur;
        // fix up
        insertFixUp(cur);
    }

    private void insertFixUp(Node node) {
        Node x = node.parent;
        while (x.color == RED) {
            updateMax(x);
            updateMax(x.parent);
            if (x == x.parent.left) {   // left cases
                x = x.parent.right;
                if (x.color == BLACK) {
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
                if (x.color == BLACK) {
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
        for (; x != nil; x = x.parent) {
            double originalMax = x.max;
            updateMax(x);
            if (x.max == originalMax) break;
        }
        root.color = BLACK;
    }

    public void delete(Interval interval) {
        Node z = root;
        while (z != nil) {
            int cmp = interval.compareTo(z.interval);
            if      (cmp < 0) z = z.left;
            else if (cmp > 0) z = z.right;
            else              break;
        }
        if (z == nil) return;
        Node x;
        Node y = z;
        boolean originalColor = y.color;
        if (z.left == nil) {
            x = z.right;
            transplant(x, z);
        } else if (z.right == nil) {
            x = z.left;
            transplant(x, z);
        } else {
            y = min(z.right);
            originalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;   // make sure that fix up will work properly
            } else {
                transplant(x, y);
                y.right = z.right;
                y.right.parent = y;
            }
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
            y.max = z.max;
        }
        if (originalColor == BLACK) deleteFixUp(x);
        else {      // only update max
            for (x = x.parent; x != nil; x = x.parent) {
                double originalMax = x.max;
                updateMax(x);
                if (x.max == originalMax) break;
            }
        }
        for (; y != nil; y = y.parent) {
            double originalMax = y.max;
            updateMax(y);
            if (y.max == originalMax) break;
        }
    }

    private void transplant(Node s, Node t) {
        if      (t.parent == nil   ) root = s;
        else if (t == t.parent.left) t.parent.left = s;
        else                         t.parent.right = s;
        s.parent = t.parent;
    }

    private void deleteFixUp(Node node) {
        while (node != root && node.color == BLACK) {
            Node x = node.parent;
            updateMax(x);
            if (node == x.left) {           // left cases
                x = x.right;                // x may be nil
                if (x.color == RED) {
                    // case 1, convert to case 2, 3 or 4
                    x.color = BLACK;      // node.parent.color must be black by Property 2
                    node.parent.color = RED;
                    rotateLeft(node.parent);
                    x = node.parent.right;    // update cur to point to node's brother
                }
                if (x.left.color == BLACK && x.right.color == BLACK) {
                    // case 2, pull black from node and its brother to node's parent
                    x.color = RED;
                    node = node.parent;
                }
                else {
                    // node's brother must has at least one red child
                    if (x.right.color == BLACK) {
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
                x = x.left;             // cur may be nil
                if (x.color == RED) {
                    // case 1, convert to case 2, 3 or 4
                    x.color = BLACK;      // node.parent.color must be black by Property 2
                    node.parent.color = RED;
                    rotateRight(node.parent);
                    x = node.parent.left;     // update cur to point to node's brother
                }
                if (x.left.color == BLACK && x.right.color == BLACK) {
                    // case 2, pull black from node and its brother to node's parent
                    x.color = RED;
                    node = node.parent;
                }
                else {
                    // node's brother must has at least one red child
                    if (x.left.color == BLACK) {
                        // case 3, move red link to left
                        x.color = RED;
                        x.right.color = BLACK;
                        rotateLeft(x);
                        x = x.parent;    // cur is node's new brother
                    }
                    // case 4, remove extra black by lending a black link from node's brother
                    node = x.parent;
                    x.color = node.color;
                    node.color = BLACK;
                    x.left.color = BLACK;         // absorb a black link
                    rotateRight(node);
                    break;
                }
            }
        }
        node.color = BLACK;
        for (node = node.parent; node != nil; node = node.parent) {
            double originalMax = node.max;
            updateMax(node);
            if (node.max == originalMax) break;
        }
    }

    private Node min(Node node) {
        if (node == nil) return null;
        while (node.left != nil) node = node.left;
        return node;
    }

    public Interval search(Interval interval) {
        Node x = root;
        while (x != nil) {
            if (x.interval.overlaps(interval)) return x.interval;
            if (x.left == nil || interval.low() > x.left.max) x = x.right;
            else x = x.left;
        }
        return x.interval;
    }

    public void print() {
        print(root);
        System.out.println();
    }

    private void print(Node node) {
        if (node == nil) {
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
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            String[] ss = s.split(" ");
            double low = Double.parseDouble(ss[0]);
            double high = Double.parseDouble(ss[1]);
            tree.delete(new Interval(low, high));
            tree.print();
        }
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            String[] ss = s.split(" ");
            double low = Double.parseDouble(ss[0]);
            double high = Double.parseDouble(ss[1]);
            Interval interval = tree.search(new Interval(low, high));
            System.out.println(interval);
        }
    }
}
