package pers.luo.algs;

/*
* delaying implementation of Prim minimum spanning tree algorithm
* do not delete invalid crossing edges until fetching them as min
*/
public class LazyPrimMST implements MST {
    private boolean[] marked;   // mark MST vertices
    private Queue<Edge> mst;    // MST edges
    private MinPQ<Edge> pq;     // priority queue to get minimum crossing edges, at least on end is in MST

    public LazyPrimMST(EdgeWeightedGraph G) {
        marked = new boolean[G.V()];
        mst    = new Queue<>();
        pq     = new MinPQ<>();

        visit(G, 0);
        while (!pq.isEmpty()) {
            Edge e = pq.delMin();   // fetch minimum crossing edge
            int v = e.either();     // v is one end (in MST or not)
            int w = e.other(v);     // w is the other end (in MST or not)
            if (marked[v] && marked[w]) continue;   // omit invalid edges
            mst.enqueue(e);         // e is the valid min crossing edge
            // note that for every edge in PQ, at least on end is in MST, so no need for two if statement
            if (!marked[v]) visit(G, v);    // if v is not in MST, visit v and add crossing edges
            else            visit(G, w);    // if w is not in MST, visit w and add crossing edges
        }
    }

    private void visit(EdgeWeightedGraph G, int v) {
        marked[v] = true;       // mark v as being in MST
        for (Edge e : G.adj(v))
            if (!marked[e.other(v)]) pq.insert(e);  // add new crossing edges to PQ
    }

    // all edges of MST
    public Iterable<Edge> edges() {
        return mst;
    }

    // weight of MST, delaying implementation
    public double weight() {
        double weight = 0.0;
        for (Edge e : mst)
            weight += e.weight();
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
