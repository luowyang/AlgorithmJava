package pers.luo.algs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Abstract data structure for ternary searching trie symbol table
 * @author Luo Wenyang
 **/
public class TST<Value> implements StringST<Value> {
    private Node<Value> root;   // root of the trie

    // data structure for nodes of the trie
    private static class Node<Value> {
        char c;
        int size;
        int height;
        Node<Value> left, mid, right;
        Value value;

        public Node(char c) {
            this.c = c;
        }
    }

    // helpers
    private int height(Node<Value> node) {
        return node == null ? 0 : node.height;
    }

    private int balanceFactor(Node<Value> node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }

    private Node<Value> rotateLeft(Node<Value> node) {
        Node<Value> x = node.right;
        // set links
        node.right = x.left;
        x.left = node;
        // set heights
        node.height = 1 + Math.max(height(node.left), height(node.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        // set sizes
        x.size = node.size;
        node.size = (node.value == null) ? 0 : 1;
        node.size += size(node.left) + size(node.mid) + size(node.right);
        return x;
    }

    private Node<Value> rotateRight(Node<Value> node) {
        Node<Value> x = node.left;
        // set links
        node.left = x.right;
        x.right = node;
        // set heights
        node.height = 1 + Math.max(height(node.left), height(node.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        // set sizes
        x.size = node.size;
        node.size = (node.value == null) ? 0 : 1;
        node.size += size(node.left) + size(node.mid) + size(node.right);
        return x;
    }

    private Node<Value> balance(Node<Value> node) {
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

    // find minimum node in the binary searching tree
    // i.e. do not go middle way
    private Node<Value> minBST(Node<Value> node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;
        return node;
    }

    // find maximum node in the binary searching tree
    // i.e. do not go middle way
    private Node<Value> maxBST(Node<Value> node) {
        if (node == null) return null;
        while (node.right != null) node = node.right;
        return node;
    }

    // delete minimum node from BST, do not go middle way
    private Node<Value> delMinBST(Node<Value> node) {
        if (node == null) return null;
        if (node.left == null) return node.right;
        node.left = delMinBST(node.left);
        // update size
        node.size = node.value == null ? 0 : 1;
        node.size += size(node.left) + size(node.mid) + size(node.right);
        // update height
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    // delete maximum node from BST, do not go middle way
    private Node<Value> delMaxBST(Node<Value> node) {
        if (node == null) return null;
        if (node.right == null) return node.left;
        node.right = delMaxBST(node.right);
        // update size
        node.size = node.value == null ? 0 : 1;
        node.size += size(node.left) + size(node.mid) + size(node.right);
        // update height
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    @Override
    public Value get(String key) {
        Node<Value> cur = root;
        int d = 0, n = key.length();
        while (cur != null && d < n) {
            char c = key.charAt(d);
            if      (c < cur.c) cur = cur.left;
            else if (c > cur.c) cur = cur.right;
            else if (++d < n)   cur = cur.mid;
        }
        if (cur == null) return null;
        return cur.value;
    }

    @Override
    public void put(String key, Value value) {
        root = put(root, key, value, 0);
    }

    private Node<Value> put(Node<Value> node, String key, Value value, int d) {
        char c = key.charAt(d);
        // if encounter null link, create a new node with the current char
        if (node == null) node = new Node<>(c);
        // branching
        if      (c < node.c) node.left = put(node.left, key, value, d);
        else if (c > node.c) node.right = put(node.right, key, value, d);
        else if (++d < key.length()) {
            node.mid = put(node.mid, key, value, d);   // d has already been incremented
        }
        else {
            // update size
            if (node.value == null) {   // add 1 key to subtree, do not set to 1 because it may has non-empty children
                if (value != null) node.size++;
            }
            else if (value == null) {   // remove a key-string pair
                node.size--;
            }
            // update value
            node.value = value;
        }
        // update size
        node.size = (node.value == null) ? 0 : 1;
        node.size += size(node.left) + size(node.mid) + size(node.right);
        // update height
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    @Override
    public void delete(String key) {
        root = delete(root, key, 0);
    }

    private Node<Value> delete(Node<Value> node, String key, int d) {
        if (node == null) return null;
        char c = key.charAt(d);
        if      (c < node.c) node.left = delete(node.left, key, d);
        else if (c > node.c) node.right = delete(node.right, key, d);
        else if (++d < key.length())
                             node.mid = delete(node.mid, key, d);
        else if (node.value != null) {
            node.value = null;
        }
        if (node.value == null && size(node.mid) == 0) {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node<Value> cur = node;
            node = minBST(node.right);
            node.right = delMinBST(cur.right);
            node.left = cur.left;
        }
        // update size
        node.size = (node.value == null) ? 0 : 1;
        node.size += size(node.left) + size(node.mid) + size(node.right);
        if (node.size == 0) return null;
        // update height
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(Node<Value> node) {
        return node == null ? 0 : node.size;
    }

    @Override
    public boolean isEmpty() {
        return size(root) == 0;
    }

    @Override
    public boolean contains(String key) {
        return get(key) != null;
    }

    @Override
    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    @Override
    public Iterable<String> keysWithPrefix(String pre) {
        // find the prefix's corresponding node
        Node<Value> cur = root;
        int d = 0, n = pre.length();
        Queue<String> queue = new Queue<>();
        while (cur != null && d < n) {
            char c = pre.charAt(d);
            if      (c < cur.c) cur = cur.left;
            else if (c > cur.c) cur = cur.right;
            else {
                if (++d == n && cur.value != null)
                    queue.enqueue(pre);
                cur = cur.mid;
            }
        }
        // cur is either null or prefix's tail char's corresponding node
        if (cur != null) collect(cur, pre, queue);
        return queue;
    }

    // collect all keys within node's subtree
    // note that pre is the common prefix reaching node from a middle link
    private void collect(Node<Value> node, String pre, Queue<String> queue) {
        if (node == null) return;
        collect(node.left, pre, queue);
        if (node.value != null) queue.enqueue(pre + node.c);
        collect(node.mid, pre + node.c, queue);     // note that the prefix of the middle subtree is one char longer
        collect(node.right, pre, queue);
    }

    @Override
    public Iterable<String> keysThatMatch(String pat) {
        Queue<String> queue = new Queue<>();
        collect(root, "", pat, queue);
        return queue;
    }

    // collect all keys within node's subtree which match the pattern string pat
    // note that pre is the common prefix reaching node from a middle link
    private void collect(Node<Value> node, String pre, String pat, Queue<String> queue) {
        if (node == null) return;
        int d = pre.length();   // next char of pat to match
        char c = pat.charAt(d);
        if (c == '.') { // wildcard
            collect(node.left, pre, pat, queue);
            if (++d == pat.length()) {
                if (node.value != null)
                    queue.enqueue(pre + node.c);
            }
            else {
                collect(node.mid, pre + node.c, pat, queue);
            }
            collect(node.right, pre, pat, queue);
        }
        else if (c < node.c) collect(node.left, pre, pat, queue);
        else if (c > node.c) collect(node.right, pre, pat, queue);
        else if (++d == pat.length()) {
            if (node.value != null) queue.enqueue(pre + node.c);
        }
        else                 collect(node.mid, pre + node.c, pat, queue);
    }

    @Override
    public String longestPrefixOf(String s) {
        Node<Value> cur = root;
        int d = 0, n = s.length();
        int length = 0;
        while (cur != null && d < n) {
            char c = s.charAt(d);
            if      (c < cur.c) cur = cur.left;
            else if (c > cur.c) cur = cur.right;
            else {
                d++;
                if (cur.value != null) length = d;
                cur = cur.mid;
            }
        }
        return s.substring(0, length);
    }

    public static void main(String[] args) throws FileNotFoundException {
        TST<Integer> trie = new TST<>();
        Scanner scanner = new Scanner(new FileInputStream(args[0]));
        int index = 0;
        while (scanner.hasNext()) {
            String s = scanner.next();
            trie.put(s, index++);
        }
        System.out.println("(size: " + trie.size() + ")");
        // test keys()
        System.out.println("test keys():");
        for (String s : trie.keys())
            System.out.println(s + " " + trie.get(s));
        Scanner stdin = new Scanner(System.in);
        // test keysWitPrefix()
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
        }
        // test delete()
        System.out.println("test delete():");
        while (stdin.hasNextLine()) {
            String query = stdin.nextLine();
            if (query.equals("exit")) break;
            trie.delete(query);
            System.out.println("(size: " + trie.size() + ")");
            for (String s : trie.keys())
                System.out.println(s + " " + trie.get(s));
        }
    }
}
