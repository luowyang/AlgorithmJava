package pers.luo.algs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class OptimalBST<K extends Comparable<K>, V> {
    private static class Node<K extends Comparable<K>, V> {
        K key;
        V value;
        Node<K, V> left, right;
        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node<K, V> root;

    private double e;

    public OptimalBST(K[] keys, V[] values, double[] p, double[] q) {
        int n = keys.length - 1;
        double[][] w = new double[n+2][n+1];
        double[][] e = new double[n+2][n+1];
        int[][] roots = new int[n+1][n+1];
        for (int i = 1; i <= n+1; i++) {
            e[i][i-1] = q[i-1];
            w[i][i-1] = q[i-1];
        }
        for (int i = 1; i <=n; i++) {
            w[i][i] = w[i][i-1] + p[i] + q[i];
            e[i][i] = e[i][i-1] + e[i+1][i] + w[i][i];
            roots[i][i] = i;
        }
        for (int l = 2; l <= n; l++)
            for (int i = 1; i + l <= n+1; i++) {
                int j = i + l - 1;
                e[i][j] = Double.POSITIVE_INFINITY;
                w[i][j] = w[i][j-1] + p[j] + q[j];
                for (int k = roots[i][j-1]; k <= roots[i+1][j]; k++) {
                    double c = e[i][k-1] + e[k+1][j] + w[i][j];
                    if (c < e[i][j]) {
                        e[i][j] = c;
                        roots[i][j] = k;
                    }
                }
            }
        root = buildTree(keys, values, roots, 1, n);
        this.e = e[1][n];
    }

    private Node<K, V> buildTree(K[] keys, V[] values, int[][] roots, int i, int j) {
        if (i > j) return null;
        if (i == j) return new Node<>(keys[i], values[i]);
        int r = roots[i][j];
        Node<K, V> node = new Node<>(keys[r], values[r]);
        node.left = buildTree(keys, values, roots, i, r-1);
        node.right = buildTree(keys, values, roots, r+1, j);
        return node;
    }

    public V get(K key) {
        Node<K, V> cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if      (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else              return cur.value;
        }
        return null;
    }

    public void print() {
        print(root);
        System.out.println();
        System.out.println("expected cost: " + e);
    }

    private void print(Node<K, V> node) {
        if (node == null) {
            System.out.print("- ");
            return;
        }
        System.out.print(node.key + " ");
        print(node.left);
        print(node.right);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(args[0]));
        int N = scanner.nextInt();
        Double[] keys = new Double[N+1];
        Integer[] values = new Integer[N+1];
        double[] p = new double[N+1];
        double[] q = new double[N+1];
        for (int i = 1; i <= N; i++) {
            keys[i] = scanner.nextDouble();
            values[i] = scanner.nextInt();
            p[i] = scanner.nextDouble();
        }
        for (int i = 0; i <= N; i++)
            q[i] = scanner.nextDouble();
        OptimalBST<Double, Integer> bst = new OptimalBST<>(keys, values, p, q);
        bst.print();
    }
}
