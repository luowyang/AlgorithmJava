package pers.luo.algs;

public interface SP {
    double distTo(int v);
    boolean hasPathTo(int v);
    Iterable<DirectedEdge> pathTo(int v);
}
