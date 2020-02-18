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

    // residual capacity of the edge going to vertex
    public double residualCapacityTo(int vertex) {
        if      (vertex == v) return flow;
        else if (vertex == w) return capacity - flow;
        else throw new IllegalArgumentException("Inconsistent edge");
    }

    // add residual flow delta to the edge going to vertex
    // forward: increase flow
    // backward: decrease flow
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
