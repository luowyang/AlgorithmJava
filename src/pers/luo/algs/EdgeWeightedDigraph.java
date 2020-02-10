package pers.luo.algs;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Weighted Directed Graph
 * @author Luo Wenyang
 */
@SuppressWarnings("unchecked")
public class EdgeWeightedDigraph {
    private final int V;                // vertex counter
    private int E;                      // edge counter
    private Bag<DirectedEdge>[] adj;    // adjacent lists

    public EdgeWeightedDigraph(int V) {
        this.V = V;
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();
    }

    public EdgeWeightedDigraph(InputStream in) {
        Scanner scanner = new Scanner(in);
        V = scanner.nextInt();
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();
        int E = scanner.nextInt();      // do not manipulate this.E manually
        for (int i = 0; i < E; i++) {
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            double weight = scanner.nextDouble();
            addEdge(v, w, weight);
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(int v, int w, double weight) {
        if (v < 0 || v >= V) throw new IllegalArgumentException("First vertex out of range");
        if (w < 0 || w >= V) throw new IllegalArgumentException("Second vertex out of range");
        addEdge(new DirectedEdge(v, w, weight));
    }

    public void addEdge(DirectedEdge e) {
        adj[e.from()].add(e);
        E++;
    }

    public Iterable<DirectedEdge> adj(int v) {
        if (v < 0 || v >= V) throw new IllegalArgumentException("Vertex out of range");
        return adj[v];
    }

    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> bag = new Bag<>();
        for (int v = 0; v < V; v++)
            for (DirectedEdge e : adj[v])
                bag.add(e);
        return bag;
    }

    public String toString() {
        StringBuilder s = new StringBuilder(V + " vertices " + E + " edges\n");
        for (int v = 0; v < V; v++) {
            s.append(v).append(": ");
            for (DirectedEdge e : adj[v])
                s.append(e.to()).append("(").append(e.weight()).append(") ");
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(System.in);
        System.out.println(G);
    }
}
