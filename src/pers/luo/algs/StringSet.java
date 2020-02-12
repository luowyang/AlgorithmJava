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

    // keys
    public Iterable<String> keys() {
        Queue<String> queue = new Queue<>();
        collect(root, new StringBuilder(""), queue);
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
