package pers.luo.algs;

import java.util.Scanner;

public class Search {
    private int[] id;
    private int[] sz;
    private int N;
    private int source;

    public Search(Graph G, int s) {
        // initialize variables
        N = G.V();
        source = s;
        id = new int[N];
        sz = new int[N];
        for (int v = 0; v < N; v++) {
            id[v] = v;
            sz[v] = 1;
        }
        // build connection array
        for (int v = 0; v < id.length; v++)
            for (int w : G.adj(v)) union(v, w);
        source = find(source);
    }

    public boolean marked(int v) {
        return find(v) == source;
    }

    public int count() {
        return sz[source];
    }

    private int find(int v) {
        int root = v;
        // find root
        while (id[root] != root) root = id[root];
        // path compression
        int w;
        while (id[v] != root) {
            w = id[v];
            id[v] = root;
            v = w;
        }
        return root;
    }

    private void union(int v, int w) {
        int vRoot = find(v);
        int wRoot = find(w);
        if (vRoot == wRoot) return;
        if (sz[vRoot] < sz[wRoot]) {
            id[vRoot] = wRoot;
            sz[wRoot] += sz[vRoot];
        }
        else {
            id[wRoot] = vRoot;
            sz[vRoot] += sz[wRoot];
        }
        N--;
    }

    public static void main(String[] args) {
        Graph graph = new Graph(System.in);
        Search search = new Search(graph, Integer.parseInt(args[0]));
        for (int v = 0; v < graph.V(); v++)
            if (search.marked(v)) System.out.print(v + " ");
        System.out.println();
    }
}
