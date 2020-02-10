package pers.luo.algs;

/*
* realtime implementation of Prim MST algorithm
* use indexed priority queue to find minimum crossing edges
*/
@SuppressWarnings("unchecked")
public class PrimMST implements MST {
    private boolean[] marked;       // mark MST vertices
    private Edge[]    edgeTo;       // edgeTo[v] is the min edge from v to MST
    private double[]  distTo;       // distTo[v] = edgeTo[v].weight()
    private IndexMinPQ<Double> pq;  // possible min valid crossing edges

    private double weight;          // total weight of MST

    public PrimMST(EdgeWeightedGraph G) {
        marked = new boolean[G.V()];
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        weight = 0.0;
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;   // initially there is no known min edges to MST
        pq = new IndexMinPQ<>(G.V());
        for (int s = 0; s < G.V(); s++)
            if (!marked[s]) prim(G, s); // run prim for all CCs to find minimum spanning forest
    }

    private void prim(EdgeWeightedGraph G, int s) {
        distTo[s] = 0.0;                // s is the root of MST
        pq.insert(s, 0.0);        // initialize PQ with s
        while (!pq.isEmpty()) {         // PQ stores the possible min valid crossing edges and will only become empty when done
            weight += pq.min();         // update weight
            visit(G, pq.delMin());      // visit closest vertex
        }
    }

    // mark v and update min edges
    private void visit(EdgeWeightedGraph G, int v) {
        marked[v] = true;                   // mark v as being in MST
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (marked[w]) continue;        // omit e if v-w is not crossing edge
            if (e.weight() < distTo[w]) {
                edgeTo[w] = e;
                distTo[w] = e.weight();
                if (pq.contains(w)) pq.change(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
    }

    @Override
    public Iterable<Edge> edges() {
        Bag<Edge> mst = new Bag<>();
        for (Edge e : edgeTo)
            mst.add(e);
        return mst;
    }

    @Override
    public double weight() {
        return weight;
    }

    public static void main(String[] args) {
        EdgeWeightedGraph G = new EdgeWeightedGraph(System.in);
        LazyPrimMST mst = new LazyPrimMST(G);
        for (Edge e : mst.edges())
            System.out.println(e);
        System.out.println(mst.weight());
    }
}
