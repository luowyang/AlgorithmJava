package pers.luo.algs;

/*
* undirected weighted edge
* represent a kind of equivalent relationship
*/
public class Edge implements Comparable<Edge> {
    private final int v;            // one of two vertices
    private final int w;            // the other vertex
    private final double weight;    // weight of the edge

    public Edge(int v, int w, double weight) {
        this.v      = v;
        this.w      = w;
        this.weight = weight;
    }

    public double weight() {
        return weight;
    }

    // get one of two vertices
    public int either() {
        return v;
    }

    // get the other vertex than v
    // eg. find the other vertex w with the adjacent list index v
    public int other(int v) {
        if      (v == this.v) return this.w;
        else if (v == this.w) return this.v;
        else throw new RuntimeException("Inconsistent edge");
    }

    // compare to another edge in weight
    public int compareTo(Edge that) {
        if      (this.weight > that.weight) return +1;
        else if (this.weight < that.weight) return -1;
        else                                return  0;
    }

    public String toString() {
        return String.format("%d-%d %.5f", v, w, weight);
    }
}
