package pers.luo.algs;

/*
 * Single-Source Longest Path Algorithm for acyclic directed graph
 */
public class AcyclicLP implements SP {
    private DirectedEdge[] edgeTo;  // last edge of shortest path
    private double[] distTo;        // distance to source

    public AcyclicLP(EdgeWeightedDigraph G, int s) {
        edgeTo = new DirectedEdge[G.V()];           // edgeTo[s] should be null, which is a default value
        distTo = new double[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.NEGATIVE_INFINITY;   // initially all vertices beyond s is unreachable and negatively infinite
        distTo[s] = 0.0;                            // source has 0.0 distance

        Topological top = new Topological(G);
        for (int v : top.order())                   // no need to handle vertices before s as their distances are negatively infinity
            tight(G, v);                            // relax vertices in topological order
    }

    // vertex tightening: tight all edges from v
    private void tight(EdgeWeightedDigraph G, int v) {
        for (DirectedEdge e : G.adj(v)) {               // all edges from v will be tightened
            int w = e.to();
            if (distTo[w] < distTo[v] + e.weight()) {   // include the case where w is unreachable
                // if s-->v->w is longer than the known longest path s-->w, update LPT
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
            }
        }
    }

    public double distTo(int v) {
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        return distTo[v] > Double.NEGATIVE_INFINITY;
    }

    public Iterable<DirectedEdge> pathTo(int v) {
        Stack<DirectedEdge> path = new Stack<>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.push(e);
        return path;
    }

    public static void main(String[] args) {
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(System.in);
        int s = Integer.parseInt(args[0]);
        AcyclicLP lp = new AcyclicLP(G, s);
        for (int v = 0; v < G.V(); v++) {
            System.out.printf("%d to %d (%f): ", s, v, lp.distTo(v));
            if (lp.hasPathTo(v))
                for (DirectedEdge e : lp.pathTo(v))
                    System.out.print(e + "  ");
            System.out.println();
        }
    }
}
