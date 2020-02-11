package pers.luo.algs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Abstract data structure for R way trie symbol table
 * @author Luo Wenyang
 **/
@SuppressWarnings("unchecked")
public class TrieST<Value> {
    private static int R = 256;     // radix
    private Node<Value> root;       // root of the trie

    private static class Node<Value> {
        private Value value = null;
        private int size = 0;
        private Node<Value>[] next = (Node<Value>[]) new Node[R];
    }

    // get the value bound to key
    // null if key does not exist
    public Value get(String key) {
        Node<Value> cur = root;
        int d = 0, n = key.length();
        while (cur != null && d < n)
            cur = cur.next[key.charAt(d++)];    // don't forget to increment d
        if (cur == null) return null;
        return cur.value;
    }

    // insert or update the value bound to key
    // equivalent to delete if new value is null
    public void put(String key, Value value) {
        root = put(root, key, value, 0);
    }

    // recursively put new key-value pair
    private Node<Value> put(Node<Value> node, String key, Value value, int d) {
        if (node == null) node = new Node<>();
        if (d == key.length()) {
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
        char c = key.charAt(d);             // char to check
        Node<Value> child = node.next[c];   // current child
        node.size -= size(child);           // subtract size of current child
        node.next[c] = put(child, key, value, d+1); // recursively put in current child's subtree
        node.size += size(node.next[c]);    // update size
        return node;
    }

    public void delete(String key) {
        root = delete(root, key, 0);
    }

    // delete key from node's subtree recursively
    private Node<Value> delete(Node<Value> node, String key, int d) {
        if (node == null) return null;
        if (d == key.length()) {
            if (node.value != null) node.size--;
            if (node.size == 0) return null;
            node.value = null;
            return node;
        }
        char c = key.charAt(d);                         // char to check
        Node<Value> child = node.next[c];               // current child
        node.size -= size(child);                       // subtract size of current child
        node.next[c] = delete(child, key, d + 1);   // recursively delete in current child's subtree
        node.size += size(node.next[c]);                // update size
        if (node.size == 0) return null;
        return node;
    }

    public int size() {
        return size(root);
    }

    private int size(Node<Value> node) {
        return node == null ? 0: node.size;
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    // recursively collect all keys within node's subtree in lexicographical order
    // pre is the string represented by the path from root to the current node
    private void collect(Node<Value> node, String pre, Queue<String> queue) {
        if (node == null) return;   // basic case
        if (node.value != null) queue.enqueue(pre);
        for (char c = 0; c < R; c++)
            collect(node.next[c], pre + c, queue);
    }

    // get all keys with pre as prefix
    public Iterable<String> keysWithPrefix(String pre) {
        // find the node bounded to the last char of pre
        Node<Value> cur = root;
        int d = 0, n = pre.length();
        while (cur != null && d < n)
            cur = cur.next[pre.charAt(d++)];
        // collect all keys with cur's subtree
        Queue<String> queue = new Queue<>();
        collect(cur, pre, queue);
        return queue;
    }

    // get all keys
    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    // get all keys which match the pattern string pat
    // . is a wildcard
    // any key longer than pat will be ignored
    public Iterable<String> keysThatMatch(String pat) {
        Queue<String> queue = new Queue<>();
        collect(root, "", pat, queue);
        return queue;
    }

    // recursive collection method for pattern matching
    // only collect children who match the pattern string pat
    private void collect(Node<Value> node, String pre, String pat, Queue<String> queue) {
        int d = pre.length();
        if (node == null) return;
        if (d == pat.length()) {
            if (node.value != null) queue.enqueue(pre);
            return;
        }
        // recursive calls
        char c = pat.charAt(d);
        if (c == '.')
            for (char k = 0; k < R; k++)
                collect(node.next[k], pre + k, pat, queue);
        else
            collect(node.next[c], pre + c, pat, queue);
    }

    // return the longest prefix of s which exists in the trie
    public String longestPrefixOf(String s) {
        Node<Value> cur = root;
        int length = 0;         // length of the longest prefix
        int d = 0, n = s.length();
        while (cur != null && d < n) {
            if (cur.value != null) length = d;
            cur = cur.next[s.charAt(d++)];
        }
        return s.substring(0, length);
    }


    public static void main(String[] args) throws FileNotFoundException {
        TrieST<Integer> trie = new TrieST<>();
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
