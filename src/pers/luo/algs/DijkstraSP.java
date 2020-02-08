package pers.luo.algs;

/**
 * Dijkstra Single-Source Shortest Path Algorithm
 * @author Luo Wenyang
 */
public class DijkstraSP implements SP {
    private DirectedEdge[] edgeTo;  // edgeTo[v] is the edge from v's parent to v
    private double[] distTo;        // distTo[v] is the distance (total weight) from s to v
    private IndexMinPQ<Double> pq;  // index min priority queue to fetch the min-dist vertex not in tree

    public DijkstraSP(EdgeWeightedDigraph G, int s) {
        // initialize data structures
        edgeTo = new DirectedEdge[G.V()];           // edgeTo[s] should be null, which is a default value
        distTo = new double[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;   // initially all vertices beyond s is unreachable
        distTo[s] = 0.0;                            // source has 0.0 distance
        pq = new IndexMinPQ<>(G.V());
        pq.insert(s, 0.0);
        while (!pq.isEmpty())       // fetch min-dist vertex, relax it and it's in SPT now
            relax(G, pq.delMin());  // edgeTo[] are maintained by its parents
    }

    // edge relaxation: test whether e:v->w introduce a shorter path to w
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {   // include the case where w is unreachable
            // if s-->v->w is shorter than the known shortest path s-->w, update SPT
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        }
    }

    // vertex relaxation: relax all edges from v
    private void relax(EdgeWeightedDigraph G, int v) {
        for (DirectedEdge e : G.adj(v)) {               // all edges from v will be relaxed
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {   // include the case where w is unreachable
                // if s-->v->w is shorter than the known shortest path s-->w, update SPT
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                // update PQ
                if (pq.contains(w)) pq.change(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
    }

    // Double.POSITIVE_INFINITY if v is unreachable, 0 if v is source
    @Override
    public double distTo(int v) {
        return distTo[v];
    }

    @Override
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    // null if v is unreachable, empty if v is source (note that edgeTo[s] == null)
    @Override
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;     // null if v is unreachable
        Stack<DirectedEdge> path = new Stack<>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.push(e);
        return path;
    }

    public static void main(String[] args) {
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(System.in);
        int s = Integer.parseInt(args[0]);
        DijkstraSP sp = new DijkstraSP(G, s);
        for (int v = 0; v < G.V(); v++) {
            System.out.printf("%d to %d (%f): ", s, v, sp.distTo(v));
            if (sp.hasPathTo(v))
                for (DirectedEdge e : sp.pathTo(v))
                    System.out.print(e + "  ");
            System.out.println();
        }
    }
}
