package pers.luo.algs;

/**
 * Bellman-Ford Single-Source Shortest Path Algorithm
 * @author Luo Wenyang
 */
public class BellmanFordSP implements SP {
    private DirectedEdge[] edgeTo;          // edgeTo[v] is the edge from v's parent to v
    private double[] distTo;                // distTo[v] is the distance (total weight) from s to v
    private Queue<Integer> queue;           // vertices to be relaxed
    private boolean[] onQ;                  // whether a vertex is on queue
    private Stack<DirectedEdge> cycle;   // record the negatively weighted cycle if it exists
    private int cost;                       // counter of relax() calls

    public BellmanFordSP(EdgeWeightedDigraph G, int s) {
        //initialize variables
        edgeTo = new DirectedEdge[G.V()];   // null by default
        distTo = new double[G.V()];
        for (int v = 0; v < G.V(); v++)     // no vertex is reachable in the beginning
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;                    // source distance is 0.0
        queue = new Queue<>();
        onQ = new boolean[G.V()];
        // initiate start state
        queue.enqueue(s);
        // main body of Bellman-Ford algorithm
        while (!queue.isEmpty() && !hasNegativeCycle()) {   // continue iteration if queue has next vertex and no negative cycle
            int v = queue.dequeue();                        // fetch next vertex v
            onQ[v] = false;                                 // v is not on queue now
            // heuristic parent check
            if (edgeTo[v] != null && onQ[edgeTo[v].from()])
                continue;                                   // only relax v if v's parent in SPT is not on queue
            relax(G, v);                                    // relax it and enqueue changed vertices
        }
    }

    // relax vertex v and enqueue any changed vertices
    // look for negative cycle every V calls
    private void relax(EdgeWeightedDigraph G, int v) {
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {   // relaxation succeeds
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if (!onQ[w]) {          // if not on queue
                    queue.enqueue(w);   // put it on queue
                    onQ[w] = true;      // it's now on queue
                }
            }
            if (cost++ % G.V() == 0)
                findNegativeCycle();    // try to find negative cycle every V times of relaxation
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

    // find the negative cycle if any exists
    private void findNegativeCycle() {
        int V = edgeTo.length;
        boolean[] marked = new boolean[V];
        boolean[] onStack = new boolean[V];
        for (int s = 0; s < V; s++)
            if (edgeTo[s] != null && !marked[s])    // s must be connected to SPT and not visited
                dfs(marked, onStack, s);
    }

    // find negative cycle with DFS
    // note that a cycle in edgeTo[] must be negative
    // this is because non-negative cycle cannot pass relaxation test
    private void dfs(boolean[] marked, boolean[] onStack, int v) {
        marked[v] = true;
        if (edgeTo[v] == null) return;  // if edgeTo[v] is null, v must be the start vertex
        onStack[v] = true;
        int w = edgeTo[v].from();
        if      (!marked[w]) dfs(marked, onStack, w);
        else if (onStack[w]) {  // v is in a cycle
            cycle = new Stack<>();
            DirectedEdge e;
            for (e = edgeTo[v]; e.from() != v; e = edgeTo[e.from()])
                cycle.push(e);
            cycle.push(e);
        }
        onStack[v] = false;
    }

    // test whether a negative cycle exists
    public boolean hasNegativeCycle() {
        return cycle != null;
    }

    // return the negative cycle (or null if none exists)
    public Iterable<DirectedEdge> negativeCycle() {
        return cycle;
    }

    public static void main(String[] args) {
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(System.in);
        int s = Integer.parseInt(args[0]);
        BellmanFordSP sp = new BellmanFordSP(G, s);
        System.out.println((sp.hasNegativeCycle()?"Has":"No") + " negative cycle");
        if (sp.hasNegativeCycle()) {
            System.out.print("Negative cycle: ");
            for (DirectedEdge e : sp.cycle)
                System.out.print(e + "  ");
            System.out.println();
            return;
        }
        for (int v = 0; v < G.V(); v++) {
            System.out.printf("%d to %d (%f): ", s, v, sp.distTo(v));
            if (sp.hasPathTo(v))
                for (DirectedEdge e : sp.pathTo(v))
                    System.out.print(e + "  ");
            System.out.println();
        }
    }
}
