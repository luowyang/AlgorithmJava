package pers.luo.algs;

/**
 * Edge of flow network
 * support abstract residual network
 *
 * @author Luo Wenyang
 */
public class FlowEdge implements Comparable<FlowEdge> {
    private final int v;
    private final int w;
    private final double capacity;
    private double flow;

    public FlowEdge(int v, int w, double capacity) {
        this.v = v;
        this.w = w;
        this.capacity = capacity;
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    public int other(int v) {
        return v == this.v ? this.w : this.v;
    }

    public double capacity() {
        return capacity;
    }

    public double flow() {
        return flow;
    }

    // forward: how much can we increase until full
    // backward: how much can we decrease until empty
    public double residualCapacityTo(int vertex) {
        if      (vertex == v) return capacity - flow;
        else if (vertex == w) return flow;
        else throw new IllegalArgumentException("Inconsistent edge");
    }

    // add residual flow delta to the edge
    // forward: increase flow，vertex must be tail
    // backward: decrease flow, vertex must be head
    public void addResidualFlowTo(int vertex, double delta) {
        if      (vertex == v) flow -= delta;
        else if (vertex == w) flow += delta;
        else throw new IllegalArgumentException("Inconsistent edge");
    }

    public String toString() {
        return String.format("%d-%d %f/%f", v, w, flow, capacity);
    }

    @Override
    public int compareTo(FlowEdge that) {
        return Double.compare(this.capacity, that.capacity);
    }
}
