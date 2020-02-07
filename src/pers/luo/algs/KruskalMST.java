package pers.luo.algs;

public class KruskalMST implements MST {
    private Queue<Edge> mst;

    public KruskalMST(EdgeWeightedGraph G) {
        mst = new Queue<>();
        UnionFind uf = new UnionFind(G.V());    // maintain a list of tree identities (to which tree a vertex belong)
        MinPQ<Edge> pq = new MinPQ<>(G.E());
        for (Edge e : G.edges()) pq.insert(e);  // initially PQ contains all edges
        // begin Kruskal algorithm
        int N = G.V() - 1;
        while (!pq.isEmpty() && mst.size() < N) {
            Edge e = pq.delMin();   // fetch minimum edge
            int v = e.either(), w = e.other(v);
            // if v and w belong to the same tree, discard e as it will introduce a cycle
            if (uf.connected(v, w)) continue;
            mst.enqueue(e);     // add e to MST
            uf.union(v, w);     // v and w now belong to the same tree
        }
    }

    @Override
    public Iterable<Edge> edges() {
        return mst;
    }

    @Override
    public double weight() {
        double weight = 0.0;
        for (Edge e : mst) weight += e.weight();
        return weight;
    }

    public static void main(String[] args) {
        EdgeWeightedGraph G = new EdgeWeightedGraph(System.in);
        KruskalMST mst = new KruskalMST(G);
        for (Edge e : mst.edges())
            System.out.println(e);
        System.out.println(mst.weight());
    }
}
