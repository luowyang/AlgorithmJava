package pers.luo.algs;

/**
 * Detect negative cycles with Bellman-Ford algorithm.
 * This is possible because Bellman-Ford with neglect any non-negative cycles.
 * Initialize distTo[] to be all 0s, as if a new source with 0-weighted edges to all vertices are added.
 * Searching for negative cycles in two graphs are equivalent
 * @author Luo Wenyang
 **/
public class NegativeCycle {
    private DirectedEdge[] edgeTo;          // edgeTo[v] is the edge from v's parent to v
    private double[] distTo;                // distTo[v] is the distance (total weight) from s to v
    private Queue<Integer> queue;           // vertices to be relaxed
    private boolean[] onQ;                  // whether a vertex is on queue
    private Stack<DirectedEdge> cycle;      // record the negatively weighted cycle if it exists
    private int cost;                       // counter of relax() calls, used to detect negative cycles

    public NegativeCycle(EdgeWeightedDigraph G) {
        //initialize variables
        edgeTo = new DirectedEdge[G.V()];   // null by default
        distTo = new double[G.V()];         // 0.0 by default
        queue = new Queue<>();
        onQ = new boolean[G.V()];
        // initiate start state
        for (int v = 0; v < G.V(); v++) {
            queue.enqueue(v);
            onQ[v] = true;
        }
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
        NegativeCycle nc = new NegativeCycle(G);
        System.out.println((nc.hasNegativeCycle()?"Has":"No") + " negative cycle");
        if (nc.hasNegativeCycle()) {
            System.out.print("Negative cycle: ");
            for (DirectedEdge e : nc.cycle)
                System.out.print(e + "  ");
            System.out.println();
            return;
        }
    }
}
