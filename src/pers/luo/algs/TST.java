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
        Node<Value> left, mid, right;
        Value value;

        public Node(char c) {
            this.c = c;
        }
    }

    @Override
    public Value get(String key) {
        Node<Value> cur = root;
        int d = 0, n = key.length();
        char c = key.charAt(d);
        while (cur != null) {
            if      (c < cur.c) cur = cur.left;
            else if (c > cur.c) cur = cur.right;
            else if (++d < n)   { cur = cur.mid; c = key.charAt(d); }
            else                return cur.value;
        }
        return null;
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
            return node;
        }
        // update size
        node.size = (node.value == null) ? 0 : 1;
        node.size += size(node.left) + size(node.right);
        return node;
    }

    @Override
    public void delete(String key) {

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
        return null;
    }

    @Override
    public Iterable<String> keysThatMatch(String pat) {
        return null;
    }

    @Override
    public String longestPrefixOf(String s) {
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException {
        TST<Integer> trie = new TST<>();
        Scanner scanner = new Scanner(new FileInputStream(args[0]));
        int index = 0;
        while (scanner.hasNext()) {
            String s = scanner.next();
            trie.put(s, index++);
        }
        System.out.println("size: " + trie.size());
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
            System.out.println("size: " + trie.size());
            for (String s : trie.keys())
                System.out.println(s + " " + trie.get(s));
        }
    }
}
