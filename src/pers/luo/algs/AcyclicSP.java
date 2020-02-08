package pers.luo.algs;

/**
 * Single-Source Shortest Path Algorithm for acyclic directed graph
 * @author Luo Wenyang
  */
public class AcyclicSP implements SP {
    private DirectedEdge[] edgeTo;  // last edge of shortest path
    private double[] distTo;        // distance to source

    public AcyclicSP(EdgeWeightedDigraph G, int s) {
        edgeTo = new DirectedEdge[G.V()];           // edgeTo[s] should be null, which is a default value
        distTo = new double[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;   // initially all vertices beyond s is unreachable
        distTo[s] = 0.0;                            // source has 0.0 distance

        Topological top = new Topological(G);
        for (int v : top.order())                   // no need to handle vertices before s as their distances are infinity
            relax(G, v);                            // relax vertices in topological order
    }

    // vertex relaxation: relax all edges from v
    private void relax(EdgeWeightedDigraph G, int v) {
        for (DirectedEdge e : G.adj(v)) {               // all edges from v will be relaxed
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {   // include the case where w is unreachable
                // if s-->v->w is shorter than the known shortest path s-->w, update SPT
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
            }
        }
    }

    @Override
    public double distTo(int v) {
        return distTo[v];
    }

    @Override
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    @Override
    public Iterable<DirectedEdge> pathTo(int v) {
        Stack<DirectedEdge> path = new Stack<>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.push(e);
        return path;
    }

    public static void main(String[] args) {
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(System.in);
        int s = Integer.parseInt(args[0]);
        AcyclicSP sp = new AcyclicSP(G, s);
        for (int v = 0; v < G.V(); v++) {
            System.out.printf("%d to %d (%f): ", s, v, sp.distTo(v));
            if (sp.hasPathTo(v))
                for (DirectedEdge e : sp.pathTo(v))
                    System.out.print(e + "  ");
            System.out.println();
        }
    }
}
