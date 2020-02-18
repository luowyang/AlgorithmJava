package pers.luo.algs;

import edu.princeton.cs.algs4.FenwickTree;

import javax.xml.crypto.dom.DOMCryptoContext;

/**
 * Ford-Fulkerson maximum flow algorithm
 */
public class FordFulkerson {
    private static final double EPSILON = 1e-11;

    private FlowNetwork G;
    private int s, t;
    private boolean[] marked;
    private FlowEdge[] edgeTo;
    private double[] capTo;
    private IndexMinPQ<Double> pq;
    private double value;

    public FordFulkerson(FlowNetwork G, int s, int t) {
        this.G = G;
        this.s = s;
        this.t = t;
    }

    public boolean bfs() {
        while (hasAugmentingPathBFS()) {
            // calculate bottleneck
            double bottle = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = edgeTo[v].other(v))
                bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
            // increase residual flow
            for (int v = t; v != s; v = edgeTo[v].other(v))
                edgeTo[v].addResidualFlowTo(v, bottle);
            value += bottle;
        }
        return isFeasible();
    }

    private boolean hasAugmentingPathBFS() {
        marked = new boolean[G.V()];
        edgeTo = new FlowEdge[G.V()];
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(s);
        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            for (FlowEdge e : G.adj(v)) {
                int w = e.other(v);
                if (!marked[w] && e.residualCapacityTo(w) > 0) {
                    marked[w] = true;
                    edgeTo[w] = e;
                    queue.enqueue(w);
                }
            }
        }
        return marked[t];
    }

    public boolean dfs() {
        while (true) {
            marked = new boolean[G.V()];
            edgeTo = new FlowEdge[G.V()];
            if (!hasAugmentingPathDFS(s)) break;
            // calculate bottleneck
            double bottle = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = edgeTo[v].other(v))
                bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
            // increase residual flow
            for (int v = t; v != s; v = edgeTo[v].other(v))
                edgeTo[v].addResidualFlowTo(v, bottle);
            value += bottle;
        }
        return isFeasible();
    }

    private boolean hasAugmentingPathDFS(int v) {
        marked[v] = true;
        for (FlowEdge e : G.adj(v)) {
            int w = e.other(v);
            if (!marked[w] && e.residualCapacityTo(w) > 0) {
                edgeTo[w] = e;
                hasAugmentingPathDFS(w);
            }
        }
        return marked[t];
    }

    public boolean fat() {
        while (hasAugmentingPathFat()) {
            // increase residual flow
            for (int v = t; v != s; v = edgeTo[v].other(v))
                edgeTo[v].addResidualFlowTo(v, capTo[t]);
            value += capTo[t];
        }
        return isFeasible();
    }

    private boolean hasAugmentingPathFat() {
        edgeTo = new FlowEdge[G.V()];
        capTo = new double[G.V()];
        for (int v = 0; v < G.V(); v++)
            capTo[v] = 0.0;
        capTo[s] = Double.POSITIVE_INFINITY;
        pq = new IndexMinPQ<>(G.V());
        pq.insert(s, capTo[s]);
        while (!pq.isEmpty())
            relax(pq.delMin());
        return capTo[t] > 0;
    }

    private void relax(int v) {
        for (FlowEdge e : G.adj(v)) {
            int w = e.other(v);
            if (capTo[w] < Math.min(capTo[v], e.residualCapacityTo(w))) {
                capTo[w] = Math.min(capTo[v], e.residualCapacityTo(w));
                edgeTo[w] = e;
                // update pq
                if (pq.contains(w)) pq.change(w, capTo[w]);
                else                pq.insert(w, capTo[w]);
            }
        }
    }

    // check local equilibrium of vertex v
    private boolean localEq(int v) {
        double flow = 0.0;
        for (FlowEdge e : G.adj(v))
            if (v == e.from()) flow -= e.flow();
            else               flow += e.flow();
        return Math.abs(flow) < EPSILON;
    }

    // check whether a flow configuration is feasible
    private boolean isFeasible() {
        for (int v = 0; v < G.V(); v++)
            for (FlowEdge e : G.adj(v))
                if (e.flow() < 0 || e.flow() > e.capacity())
                    return false;
        for (int v = 0; v < G.V(); v++)
            if (v != s && v != t && !localEq(v))
                return false;
        return true;
    }

    public double value() {
        return value;
    }

    // check whether v is in the s-cut
    public boolean inCut(int v) {
        return marked[v];
    }

    public static void main(String[] args) {
        int s = Integer.parseInt(args[1]);
        int t = Integer.parseInt(args[2]);
        FlowNetwork G = new  FlowNetwork(System.in);
        FordFulkerson ff = new FordFulkerson(G, s, t);
        boolean suc = false;
        if      (args[0].equals("bfs")) suc = ff.bfs();
        else if (args[0].equals("dfs")) suc = ff.dfs();
        else if (args[0].equals("fat")) suc = ff.fat();
        System.out.printf("Max flow from %d to %d:\n", s, t);
        if (!suc) {
            System.out.println("No feasible max flow");
            return;
        }
        System.out.println("Max flow value: " + ff.value());
        for (int v = 0; v < G.V(); v++)
            for (FlowEdge e : G.adj(v))
                if (v == e.from())
                    System.out.println(e);
    }
}
