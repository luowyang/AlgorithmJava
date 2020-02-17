package pers.luo.algs;

/**
 * Ford-Fulkerson maximum flow algorithm
 */
public class FordFulkerson {
    private static final double EPSILON = 1e-11;



    // check local equilibrium of vertex v
    private boolean localEq(FlowNetwork G, int v) {
        double flow = 0.0;
        for (FlowEdge e : G.adj(v))
            if (v == e.from()) flow -= e.flow();
            else               flow += e.flow();
        return Math.abs(flow) < EPSILON;
    }

    // check whether a flow configuration is feasible
    private boolean isFeasible(FlowNetwork G, int s, int t) {
        for (int v = 0; v < G.V(); v++)
            for (FlowEdge e : G.adj(v))
                if (e.flow() < 0 || e.flow() > e.capacity())
                    return false;
        for (int v = 0; v < G.V(); v++)
            if (v != s && v != t && !localEq(G, v))
                return false;
        return true;
    }
}
