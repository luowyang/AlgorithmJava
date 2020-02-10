package pers.luo.algs;

/**
 * Lazy Dijkstra Single-Source Shortest Path Algorithm
 * @author Luo Wenyang
 */
public class LazyDijkstraSP implements SP {
    private DirectedEdge[] edgeTo;  // edges of SPT
    private double[] distTo;        // shortest distance
    MinPQ<Item> pq;                 // priority queue to pick shortest path

    private static class Item implements Comparable<Item> {
        DirectedEdge e;
        double dist;
        public Item(DirectedEdge e, double dist) {
            this.e = e;
            this.dist = dist;
        }
        @Override
        public int compareTo(Item that) {
            return Double.compare(this.dist, that.dist);
        }
    }

    public LazyDijkstraSP(EdgeWeightedDigraph G, int s) {
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;
        pq = new MinPQ<>();
        pq.insert(new Item(null, 0.0));
        while (!pq.isEmpty()) {
            Item item = pq.delMin();
            DirectedEdge e = item.e;
            if (e != null && distTo[e.to()] < Double.POSITIVE_INFINITY)
                continue;
            int v = (e == null ? s : e.to());
            edgeTo[v] = e;
            distTo[v] = item.dist;
            for (DirectedEdge de : G.adj(v))
                pq.insert(new Item(de, distTo[v]+de.weight()));
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

    public static void main(String[] args) {
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(System.in);
        int s = Integer.parseInt(args[0]);
        LazyDijkstraSP sp = new LazyDijkstraSP(G, s);
        for (int v = 0; v < G.V(); v++) {
            System.out.printf("%d to %d (%f): ", s, v, sp.distTo(v));
            if (sp.hasPathTo(v))
                for (DirectedEdge e : sp.pathTo(v))
                    System.out.print(e + "  ");
            System.out.println();
        }
    }
}
