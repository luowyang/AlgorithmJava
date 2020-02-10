package pers.luo.algs;

import java.util.Scanner;

public class DepthFirstSearch {
    private boolean[] marked;
    private int count;

    public DepthFirstSearch(Graph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    // recursively visit every unvisited neighbour
    private void dfs(Graph G, int v) {
        marked[v] = true;   // only called for unvisited neighbours so first mark current vertex
        count++;            // counter of visited vertices increment
        for (int w : G.adj(v))
            if (!marked[w]) dfs(G, w);
    }

    public boolean marked(int v) {
        if (v < 0 || v >= marked.length) throw new IllegalArgumentException("Vertex out of range");
        return marked[v];
    }

    public int count() {
        return count;
    }

    public static void main(String[] args) {
        Graph G = new Graph(System.in);
        DepthFirstSearch dfs = new DepthFirstSearch(G, Integer.parseInt(args[0]));
        for (int v = 0; v < G.V(); v++)
            if (dfs.marked(v)) System.out.print(v + " ");
        System.out.println();
    }
}
