package pers.luo.algs;

import java.io.InputStream;
import java.util.Scanner;


/*
* this is the data representation of directed graph with ADJACENT LISTS
*/
@SuppressWarnings("unchecked")
public class Digraph {
    private final int V;
    private int E;
    private Bag<Integer>[] adj;

    // construct an empty digraph with V vertices
    public Digraph(int V) {
        this.V = V; this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();
    }

    // construct a digraph from input stream
    public Digraph(InputStream in) {
        Scanner scanner = new Scanner(in);
        V = scanner.nextInt();
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();
        int E = scanner.nextInt();  // do not update this.E manually
        for (int i = 0; i < E; i++) {
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            addEdge(v, w);
        }
    }

    public Digraph(Digraph that) {
        if (that == null) throw new IllegalArgumentException("Null argument for digraph copy constructor");
        this.V = that.V;
        this.E = that.V;
        this.adj = (Bag<Integer>[]) new Bag[this.V];
        for (int v = 0; v < this.V; v++)
            this.adj[v] = new Bag<>(that.adj[v]);
    }

    // return number of vertices
    public int V() {
        return V;
    }

    // return number of edges
    public int E() {
        return E;
    }

    public int outDegree(int v) {
        if (v < 0 || v >= V) throw new IllegalArgumentException("Vertex out of range");
        return adj[v].size();
    }

    // add a directed edge from vertices v to w
    public void addEdge(int v, int w) {
        if (v < 0 || v >= V) throw new IllegalArgumentException("First vertex out of range");
        if (w < 0 || w >= V) throw new IllegalArgumentException("Second vertex out of range");
        adj[v].add(w);  // only add the directed edge in v's adjacent list
        E++;
    }

    // whether edge v -> w exists
    public boolean hasEDge(int v, int w) {
        if (v < 0 || v >= V) throw new IllegalArgumentException("First vertex out of range");
        if (w < 0 || w >= V) throw new IllegalArgumentException("Second vertex out of range");
        return adj[v].contains(w);
    }

    // all adjacent vertices reachable from v
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    // return the reverse digraph of this digraph
    // used to find from which vertices vertex v is pointed to
    public Digraph reverse() {
        Digraph R = new Digraph(V);
        for (int v = 0; v < V; v++)
            for (int w : adj[v])
                R.addEdge(w, v);    // store reverse edges in R
        return R;
    }

    // string representation of this digraph
    public String toString() {
        StringBuilder s = new StringBuilder(V + " vertices, " + E + " edges\n");
        for (int v = 0; v < V; v++) {
            s.append(v).append(": ");
            for (int w : adj[v])
                s.append(w).append(" ");
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        Digraph G = new Digraph(System.in);
        System.out.println("Digraph:");
        System.out.println(G);
        System.out.println("Reverse digraph:");
        Digraph R = new Digraph(G.reverse());
        System.out.println(R);
    }
}
