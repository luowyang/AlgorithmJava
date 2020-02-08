package pers.luo.algs;

/**
 * Weighted Directed Edge v->w
 * @author Luo Wenyang
 */
public class DirectedEdge {
    private final int v;            // from v
    private final int w;            // to w
    private final double weight;    // weight of the edge

    public DirectedEdge(int v, int w, double weight) {
        this.v      = v;
        this.w      = w;
        this.weight = weight;
    }

    public double weight() {
        return weight;
    }

    // return v
    public int from() {
        return v;
    }

    // return w
    public int to() {
        return w;
    }

    public String toString() {
        return String.format("%d->%d %f", v, w, weight);
    }
}
