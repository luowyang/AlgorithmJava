package pers.luo.algs;

import java.io.InputStream;

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

    /*public FlowNetwork(InputStream in) {

    }*/

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
        addEdge(new FlowEdge(v, w, weight));
    }

    public void addEdge(FlowEdge e) {
        int v = e.from();     // get one vertex
        int w = e.other(v);     // get the other vertex
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
