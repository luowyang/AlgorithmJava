package pers.luo.algs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/*
* Edge Weighted Graph
*/
@SuppressWarnings("unchecked")
public class EdgeWeightedGraph {
    private final int V;        // vertices counter
    private int E;              // edges counter
    private Bag<Edge>[] adj;    // adjacent lists

    public EdgeWeightedGraph(int V) {
        this.V = V;
        // initialize adjacent lists
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();
    }

    public EdgeWeightedGraph(InputStream in) {
        Scanner scanner = new Scanner(in);
        V = scanner.nextInt();  // read V
        // initialize adjacent lists
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();
        int E = scanner.nextInt();  // read E, do not manipulate this.E manually
        for (int i = 0; i < E; i++) {
            int v = scanner.nextInt();              // read v
            int w = scanner.nextInt();              // read w
            double weight = scanner.nextDouble();   // read weight
            addEdge(v, w, weight);
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    // add new edge v-w with weight
    public void addEdge(int v, int w, double weight) {
        if (v < 0 || v >= V) throw new IllegalArgumentException("First vertex out of range");
        if (w < 0 || w >= V) throw new IllegalArgumentException("Second vertex out of range");
        addEdge(new Edge(v, w, weight));
    }

    // add edge e
    public void addEdge(Edge e) {
        int v = e.either();     // get one vertex
        int w = e.other(v);     // get the other vertex
        adj[v].add(e);          // add new edge to v's adjacent list
        adj[w].add(e);          // add new edge to w's adjacent list
        E++;                    // increment edge counter
    }

    // iterate on adjacent list
    public Iterable<Edge> adj(int v) {
        if (v < 0 || v >= V) throw new IllegalArgumentException("Vertex out of range");
        return adj[v];
    }

    // iterate on all edges
    public Iterable<Edge> edges() {
        Bag<Edge> edges = new Bag<>();
        for (int v = 0; v < V; v++)
            for (Edge e : adj[v])
                if (e.other(v) > v) edges.add(e);   // omit self-loop, only add edge once
        return edges;
    }

    public String toString() {
        StringBuilder s = new StringBuilder(V + " vertices " + E + " edges\n");
        for (int v = 0; v < V; v++) {
            s.append(v).append(": ");
            for (Edge e : adj[v])
                s.append(e.other(v)).append("(").append(e.weight()).append(") ");
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        EdgeWeightedGraph G = new EdgeWeightedGraph(System.in);
        System.out.println(G);
    }
}
