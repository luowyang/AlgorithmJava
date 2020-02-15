package pers.luo.algs;

import edu.princeton.cs.algs4.BinaryOut;
import edu.princeton.cs.algs4.BinaryStdIn;

/**
 * Huffman encoding compression algorithm
 * @author Luo Wenyang
 */
public class Huffman {
    private static final int R = 256;

    // implement comparable interface to allow comparing frequency
    private static class Node implements Comparable<Node> {
        char ch;    // char of the leaf node
        int freq;   // frequency of the char in the text
        final Node left, right;
        public Node(char ch, int freq, Node  left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }
        public boolean isLeaf() {
            return (left == null) && (right == null);
        }
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }

    // expand bit stream
    public static void expand(String bytes) {
        BinaryOut out = new BinaryOut(bytes);
        Node root = readTrie();
        int N = BinaryStdIn.readInt();      // number of characters
        for (int i = 0; i < N; i++) {
            Node cur = root;
            while (!cur.isLeaf()) {
                if (BinaryStdIn.readBoolean())
                    cur = cur.right;
                else
                    cur = cur.left;
            }
            out.write(cur.ch);
        }
        out.close();
    }

    // read the huffman trie from bit stream in pre-order
    private static Node readTrie() {
        if (BinaryStdIn.readBoolean())  // leaf
            return new Node(BinaryStdIn.readChar(), 0,null, null);
        return new Node('\0', 0, readTrie(), readTrie());
    }

    // compress byte stream
    public static void compress(String bits) {
        BinaryOut out = new BinaryOut(bits);
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();
        // count frequencies
        int[] freq = new int[R];
        for (char c : input)
            freq[c]++;
        // build trie
        Node root = buildTrie(freq);
        // build codeword table
        String[] st = buildCode(root);
        // write trie to bit stream
        writeTrie(root, out);
        // write total number of chars
        out.write(root.freq);
        // encode input
        for (char c : input) {
            String code = st[c];
            for (int j = 0; j < code.length(); j++)
                if (code.charAt(j) == '1')
                    out.write(true);   // write 1
                else
                    out.write(false);  // write 0
        }
        out.close();
    }

    // build trie from frequencies
    private static Node buildTrie(int[] freq) {
        MinPQ<Node> pq = new MinPQ<>();
        // create add insert leaves
        for (char c = 0; c < R; c++) {
            if (freq[c] > 0)    // only deal with existing chars
                pq.insert(new Node(c, freq[c], null, null));
        }
        while (pq.size() > 1) {
            Node x = pq.delMin();
            Node y = pq.delMin();
            Node parent = new Node('\0', x.freq + y.freq, x, y);    // left right order doesn't matter
            pq.insert(parent);
        }
        return pq.delMin();
    }

    // write trie in bit stream with pre-order traverse
    private static void writeTrie(Node node, BinaryOut out) {
        if (node.isLeaf()) {    // leaf is 1 followed by ASCII
            out.write(true);
            out.write(node.ch);
            return;
        }
        // write and then traverse down
        out.write(false);  // internal node is 0
        writeTrie(node.left, out);
        writeTrie(node.right, out);
    }

    // build codeword table
    private static String[] buildCode(Node root) {
        String[] st = new String[R];
        StringBuilder s = new StringBuilder();
        buildCode(root, st, s);
        return st;
    }

    // recursive method using DFS
    // s is the code to the node
    private static void buildCode(Node node, String[] st, StringBuilder s) {
        if (node.isLeaf()) {
            st[node.ch] = s.toString();
            return;
        }
        buildCode(node.left, st, s.append('0'));
        s.deleteCharAt(s.length() - 1);
        buildCode(node.right, st, s.append('1'));
        s.deleteCharAt(s.length() - 1);
    }

    public static void main(String[] args) {
        if (args[0].equals("-"))
            compress(args[1]);
        else
            expand(args[1]);
    }
}
