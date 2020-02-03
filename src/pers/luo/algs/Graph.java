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

    // get all adjacent vertices of vertex v, sequence undefined
    // adj() method is the foundation of graph algorithms
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    // adjacent lists representation of graph
    public String toString() {
        String s = V + " vertices, " + E + " edges\n";
        for (int v = 0; v < V; v++) {
            s += v + ": ";
            for (int w : this.adj(v))
                s += w + " ";
            s += "\n";
        }
        return s;
    }

    public static void main(String[] args) {
        Graph graph = new Graph(System.in);
        System.out.println(graph);
    }
}
