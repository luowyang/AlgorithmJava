package pers.luo.algs;

/*
 * Boruvka minimum spanning tree algorithm
 */
public class BoruvkaMST implements MST {
    private Bag<Edge> mst;    // edges of MST

    private double weight;      // total weight of MST

    public BoruvkaMST(EdgeWeightedGraph G) {
        mst = new Bag<>();
        weight = 0.0;
        UnionFind uf = new UnionFind(G.V());    // record to which tree each vertex belongs
        int N = G.V() - 1;
        for (int t = 1; t < G.V() && mst.size() < N; t += t) {  // t can be interpreted as lower bound of tree size
            // go through all edges to find closest edge of every tree to other trees
            Edge[] closest = new Edge[G.V()];  // clear closest[]
            for (Edge e : G.edges()) {
                int v = e.either(), w = e.other(v);
                int i = uf.find(v), j = uf.find(w);
                if (i == j) continue;   // if e connects the same tree, omit it
                if (closest[i] == null || less(e, closest[i])) closest[i] = e; // if e is closer than closest[i], update it
                if (closest[j] == null || less(e, closest[j])) closest[j] = e; // if e is closer than closest[j], update it
            }
            // add closets edges to MST
            for (Edge e : closest) {
                if (e == null) continue;
                int v = e.either(), w = e.other(v);
                if (uf.connected(v, w)) continue;   // prevent adding same edge multiple times
                mst.add(e);                     // add e to MST
                weight += e.weight();               // update weight
                uf.union(v, w);                     // v and w now belong to the same tree
            }
        }
    }

    private boolean less(Edge first, Edge second) {
        return first.compareTo(second) < 0;
    }

    @Override
    public Iterable<Edge> edges() {
        return mst;
    }

    @Override
    public double weight() {
        return weight;
    }

    public static void main(String[] args) {
        EdgeWeightedGraph G = new EdgeWeightedGraph(System.in);
        BoruvkaMST mst = new BoruvkaMST(G);
        for (Edge e : mst.edges())
            System.out.println(e);
        System.out.println(mst.weight());
    }
}
