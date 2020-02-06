package pers.luo.algs;

public class LazyPrimMST implements MST {
    private double weight;

    public LazyPrimMST(EdgeWeightedGraph G) {

    }

    // all edges of MST
    public Iterable<Edge> edges() {

    }

    // weight of MST
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
