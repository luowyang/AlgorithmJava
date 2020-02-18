package pers.luo.algs;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Flow network
 *
 * @author Luo Wenyang
 */
@SuppressWarnings("unchecked")
public class FlowNetwork {
    private final int V;
    private int E;
    private Bag<FlowEdge>[] adj;

    public FlowNetwork(int V) {
        this.V = V;
        adj = (Bag<FlowEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();
    }

    public FlowNetwork(InputStream in) {
        Scanner scanner = new Scanner(in);
        V = scanner.nextInt();  // read V
        // initialize adjacent lists
        adj = (Bag<FlowEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();
        int E = scanner.nextInt();  // read E, do not manipulate this.E manually
        for (int i = 0; i < E; i++) {
            int v = scanner.nextInt();              // read v
            int w = scanner.nextInt();              // read w
            double cap = scanner.nextDouble();   // read weight
            addEdge(v, w, cap);
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    // add new flow edge v->w with cap
    public void addEdge(int v, int w, double cap) {
        if (v < 0 || v >= V) throw new IllegalArgumentException("First vertex out of range");
        if (w < 0 || w >= V) throw new IllegalArgumentException("Second vertex out of range");
        addEdge(new FlowEdge(v, w, cap));
    }

    public void addEdge(FlowEdge e) {
        int v = e.from();     // get one vertex
        int w = e.to();     // get the other vertex
        adj[v].add(e);          // add new edge to v's adjacent list
        adj[w].add(e);          // add new edge to w's adjacent list
        E++;                    // increment edge counter
    }

    public Iterable<FlowEdge> adj(int v) {
        if (v < 0 || v >= V) throw new IllegalArgumentException("Vertex out of range");
        return adj[v];
    }

    public Iterable<FlowEdge> edges() {
        Bag<FlowEdge> edges = new Bag<>();
        for (int v = 0; v < V; v++)
            for (FlowEdge e : adj[v])
                if (e.other(v) > v) edges.add(e);   // omit self-loop, only add edge once
        return edges;
    }

    public String toString() {
        StringBuilder s = new StringBuilder(V + " vertices " + E + " edges\n");
        for (int v = 0; v < V; v++) {
            s.append(v).append(": ");
            for (FlowEdge e : adj[v])
                s.append(e.other(v)).append("(").append(e.flow()).append("/").append(e.capacity()).append(") ");
            s.append("\n");
        }
        return s.toString();
    }
}
