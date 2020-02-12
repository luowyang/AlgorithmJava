package pers.luo.algs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * String Set based on ternary searching trie
 * @author Luo Wenyang
 **/
public class StringSet {
    private Node root;

    private static class Node {
        char c;
        boolean isKey;
        int size;
        int height;
        Node left, mid, right;

        public Node(char c) {
            this.c = c;
        }
    }

    // helpers
    private int size(Node node) {
        return node == null ? 0 : node.size;
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int balanceFactor(Node node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }

    private Node rotateLeft(Node node) {
        Node x = node.right;
        // set links
        node.right = x.left;
        x.left = node;
        // update height
        node.height = 1 + Math.max(height(node.left), height(node.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        // set sizes
        x.size = node.size;
        node.size = node.isKey ? 1 : 0;
        node.size += size(node.left) + size(node.mid) + size(node.right);
        return x;
    }

    private Node rotateRight(Node node) {
        Node x = node.left;
        // set links
        node.left = x.right;
        x.right = node;
        // update height
        node.height = 1 + Math.max(height(node.left), height(node.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        // set sizes
        x.size = node.size;
        node.size = node.isKey ? 1 : 0;
        node.size += size(node.left) + size(node.mid) + size(node.right);
        return x;
    }

    private Node balance(Node node) {
        int bf = balanceFactor(node);
        if (bf > 1) {
            if (balanceFactor(node.left) < 0) node.left = rotateLeft(node.left);
            node = rotateRight(node);
        }
        else if (bf < -1) {
            if (balanceFactor(node.right) > 0) node.right = rotateRight(node.right);
            node = rotateLeft(node);
        }
        return node;
    }

    // add
    public void add(String key) {
        root = add(root, key, 0);
    }

    private Node add(Node node, String key, int d) {
        char c = key.charAt(d);
        if (node == null) node = new Node(c);
        if      (c < node.c) node.left = add(node.left, key, d);
        else if (c > node.c) node.right = add(node.right, key, d);
        else if (++d < key.length()) {
            node.mid = add(node.mid, key, d);
        }
        else {
            if (!node.isKey) node.size++;
            node.isKey = true;
        }
        node.size = node.isKey ? 1 : 0;
        node.size += size(node.left) + size(node.mid) + size(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    // delete
    public void delete(String key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node node, String key, int d) {
        if (node == null) return null;
        char c = key.charAt(d);
        if      (c < node.c) node.left = delete(node.left, key, d);
        else if (c > node.c) node.right = delete(node.right, key, d);
        else if (++d < key.length()) {
            node.mid = delete(node.mid, key, d);
        }
        else if (node.isKey) {
            node.isKey = false;
        }
        if (!node.isKey && size(node.mid) == 0) {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            // TODO: both left and right not null
        }
        node.size = node.isKey ? 1 : 0;
        node.size += size(node.left) + size(node.mid) + size(node.right);
        if (node.size == 0) return null;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    // contains
    public boolean contains(String key) {
        Node cur = root;
        int d = 0, n = key.length();
        while (cur != null && d < n) {
            char c = key.charAt(d);
            if      (c < cur.c) cur = cur.left;
            else if (c > cur.c) cur = cur.right;
            else if (++d < n)   cur = cur.mid;
        }
        return cur != null && cur.isKey;
    }

    // isEmpty
    public boolean isEmpty() {
        return size(root) == 0;
    }

    // size
    public int size() {
        return size(root);
    }

    // containsPrefix
    public boolean containsPrefix(String s) {
        Node cur = root;
        int d = 0, n = s.length();
        while (cur != null && d < n) {
            char c = s.charAt(d);
            if      (c < cur.c) cur = cur.left;
            else if (c > cur.c) cur = cur.right;
            else if (++d < n)   cur = cur.mid;
        }
        if (cur == null) return false;
        return cur.isKey || size(cur.mid) > 0;
    }

    // min
    public String min() {
        return min(root, new StringBuilder());
    }

    private String min(Node node, StringBuilder pre) {
        if (node == null) return null;
        while (node != null) {
            while (node.left != null) node = node.left;
            pre.append(node.c);
            if (node.isKey) return pre.toString();
            node = node.mid;
        }
        return null;
    }

    // max
    public String max() {
        return max(root, new StringBuilder());
    }

    private String max(Node node, StringBuilder pre) {
        if (node == null) return null;
        if (node.right != null) return max(node.right, pre);
        pre.append(node.c);
        if (node.mid == null) return pre.toString();
        return max(node.mid, pre);
    }

    // floor
    public String floor(String key) {
        return floor(root, key, new StringBuilder(), 0);
    }

    private String floor(Node node, String key, StringBuilder pre, int d) {
        if (node == null) return null;
        char c = key.charAt(d);
        if (c < node.c) return floor(node.left, key, pre, d);
        String s;
        if (c > node.c) {
            s = floor(node.right, key, pre, d);
            if (s == null) s = max(node.mid, pre.append(node.c));
            if (s == null && node.isKey) return pre.toString();
            pre.deleteCharAt(pre.length() - 1);
            if (s == null) return max(node.left, pre);
        }
        else if (++d < key.length()) {
            s = floor(node.mid, key, pre.append(node.c), d);
            if (s == null && node.isKey) return pre.toString();
            pre.deleteCharAt(pre.length() - 1);
            if (s == null) return max(node.left, pre);
        }
        else if (node.isKey) return pre.toString() + node.c;
        else return max(node.left, pre);
        return s;
    }

    // ceiling
    public String ceiling(String key) {
        return ceiling(root, key, new StringBuilder(), 0);
    }

    private String ceiling(Node node, String key, StringBuilder pre, int d) {
        if (node == null) return null;
        char c = key.charAt(d);
        if (c > node.c) return ceiling(node.right, key, pre, d);
        String s;
        if (c < node.c) {
            s = ceiling(node.left, key, pre, d);
            if (s == null) s = min(node.mid, pre.append(node.c));
            if (s == null && node.isKey) return pre.toString();
            pre.deleteCharAt(pre.length() - 1);
            if (s == null) return min(node.right, pre);
        }
        else if (++d < key.length()) {
            s = ceiling(node.mid, key, pre.append(node.c), d);
            if (s == null && node.isKey) return pre.toString();
            pre.deleteCharAt(pre.length() - 1);
            if (s == null) return min(node.right, pre);
        }
        else if (node.isKey) return pre.toString() + node.c;
        else {
            s = min(node.mid, pre.append(node.c));
            pre.deleteCharAt(pre.length() - 1);
            if (s == null) return min(node.right, pre);
        }
        return s;
    }

    // rank
    public int rank(String key) {
        Node cur = root;
        int d = 0, n = key.length();
        int r = 0;
        while (cur != null && d < n) {
            char c = key.charAt(d);
            if      (c < cur.c) cur = cur.left;
            else if (c > cur.c) {
                if (cur.isKey) r++;
                r += size(cur.left) + size(cur.mid);
                cur = cur.right;
            }
            else {
                if (++d < n && cur.isKey) r++;
                r += size(cur.left);
                cur = cur.mid;
            }
        }
        return r;
    }

    // select
    public String select(int k) {
        StringBuilder s = new StringBuilder();
        Node cur = root;
        while (cur != null && k >= 0) {
            if      (size(cur.left) > k) cur = cur.left;
            else if (size(cur.left) == k) {
                s.append(cur.c);
                if (cur.isKey) return s.toString();
                if (size(cur.mid) > 0) return min(cur.mid, s);
                s.deleteCharAt(s.length() - 1);
                return min(cur.right, s);
            }
            else if (k < size(cur) - size(cur.right)) {
                if (cur.isKey) k--;
                k -= size(cur.left);
                s.append(cur.c);
                cur = cur.mid;
            }
            else {
                k -= size(cur) - size(cur.right);
                cur = cur.right;
            }
        }
        if (cur == null) return null;
        return s.toString();
    }

    // keys
    public Iterable<String> keys() {
        Queue<String> queue = new Queue<>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    private void collect(Node node, StringBuilder pre, Queue<String> queue) {
        if (node == null) return;
        collect(node.left, pre, queue);
        if (node.isKey) queue.enqueue(pre.toString() + node.c);
        collect(node.mid, pre.append(node.c), queue);
        pre.deleteCharAt(pre.length() - 1);
        collect(node.right, pre, queue);
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        for (String s : keys())
            joiner.add(s);
        return "{" + joiner.toString() + "}";
    }

    public static void main(String[] args) throws FileNotFoundException {
        StringSet set = new StringSet();
        Scanner scanner = new Scanner(new FileInputStream(args[0]));
        while (scanner.hasNext()) {
            String s = scanner.next();
            set.add(s);
        }
        System.out.println("(size: " + set.size() + ")");
        // test toString() and keys()
        System.out.println("test toString():");
        System.out.println(set);
        Scanner stdin = new Scanner(System.in);
        /*// test keysWitPrefix()
        System.out.println("test keysWithPrefix():");
        while (stdin.hasNextLine()) {
            String query = stdin.nextLine();
            if (query.equals("exit")) break;
            for (String s : trie.keysWithPrefix(query))
                System.out.println(s + " " + trie.get(s));
        }
        // test keysThatMatch()
        System.out.println("test keysThatMatch():");
        while (stdin.hasNextLine()) {
            String query = stdin.nextLine();
            if (query.equals("exit")) break;
            for (String s : trie.keysThatMatch(query))
                System.out.println(s + " " + trie.get(s));
        }
        // test longestPrefixOf()
        System.out.println("test longestPrefixOf():");
        while (stdin.hasNextLine()) {
            String query = stdin.nextLine();
            if (query.equals("exit")) break;
            System.out.println(" " + trie.longestPrefixOf(query));
        }*/
        // test min()
        System.out.println("test min(): " + set.min());
        // test max()
        System.out.println("test max(): " + set.max());
        // test floor()
        System.out.println("test floor():");
        while (stdin.hasNextLine()) {
            String query = stdin.nextLine();
            if (query.equals("exit")) break;
            System.out.println(set.floor(query));
        }
        // test ceiling()
        System.out.println("test ceiling():");
        while (stdin.hasNextLine()) {
            String query = stdin.nextLine();
            if (query.equals("exit")) break;
            System.out.println(set.ceiling(query));
        }
        // test rank()
        System.out.println("test rank():");
        while (stdin.hasNextLine()) {
            String query = stdin.nextLine();
            if (query.equals("exit")) break;
            System.out.println(set.rank(query));
        }
        // test select()
        System.out.println("test select():");
        while (stdin.hasNextLine()) {
            String query = stdin.nextLine();
            if (query.equals("exit")) break;
            System.out.println(set.select(Integer.parseInt(query)));
        }
        // test containsPrefix()
        System.out.println("test containsPrefix():");
        while (stdin.hasNextLine()) {
            String query = stdin.nextLine();
            if (query.equals("exit")) break;
            System.out.println(set.containsPrefix(query));
        }
        // test delete()
        System.out.println("test delete():");
        while (stdin.hasNextLine()) {
            String query = stdin.nextLine();
            if (query.equals("exit")) break;
            set.delete(query);
            System.out.println("(size: " + set.size() + ")");
            System.out.println(set);
        }
    }
}
