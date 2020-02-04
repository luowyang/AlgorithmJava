package pers.luo.algs;

import java.io.InputStream;
import java.util.Scanner;

@SuppressWarnings("unchecked")
public class Graph {
    private final int V;        // total number of vertices
    private int E;              // counter of edges
    private Bag<Integer>[] adj; // adjacent lists

    // build a graph with V vertices and no edges
    public Graph(int V) {
        this.V = V; this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];  // initialize array of lists
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();   // initialize lists
    }

    // read a graph from input stream
    // input format: V E edge-pairs, 2E + 2 parameters in total
    public Graph(InputStream in) {
        Scanner scanner = new Scanner(in);
        this.V = scanner.nextInt();
        adj = (Bag<Integer>[]) new Bag[V];  // initialize array of lists
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();   // initialize lists
        int E = scanner.nextInt();  // do not change this.E manually, let addEdge manage it
        for (int i = 0; i < E; i++) {
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            addEdge(v, w);
        }
    }

    // copy constructor, deep copy
    public Graph(Graph G) {
        if (G == null) throw new IllegalArgumentException("Null argument for graph copy constructor");
        V = G.V;
        E = G.E;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>(G.adj[v]);   // copy-construct bags
    }

    // # of vertices
    public int V() {
        return V;
    }

    // # of edges
    public int E() {
        return E;
    }

    // add an edge between vertices v and w
    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
        E++;
    }

    // return whether edge v-w exists
    public boolean hasEdge(int v, int w) {
        if (adj[v].size() > adj[w].size())
            return adj[w].contains(v);
        else
            return adj[v].contains(w);
    }

    // get all adjacent vertices of vertex v, sequence undefined
    // adj() method is the foundation of graph algorithms
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    // adjacent lists representation of graph
    public String toString() {
        StringBuilder s = new StringBuilder(V + " vertices, " + E + " edges\n");
        for (int v = 0; v < V; v++) {
            s.append(v).append(": ");
            for (int w : this.adj(v))
                s.append(w).append(" ");
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        Graph graph = new Graph(System.in);
        System.out.println(graph);
    }
}
