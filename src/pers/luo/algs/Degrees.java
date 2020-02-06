package pers.luo.algs;

public class Degrees {
    private int[] inDegree;
    private int[] outDegree;
    private boolean isMap = true;

    public Degrees(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Null digraph");
        inDegree = new int[G.V()];
        outDegree = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v))
                inDegree[w]++;
            outDegree[v] = G.outDegree(v);
            if (outDegree[v] != 1) isMap = false;
        }
    }

    public int inDegree(int v) {
        if (v < 0 || v >= inDegree.length) throw new IllegalArgumentException("Vertex out of range");
        return inDegree[v];
    }

    public int outDegree(int v) {
        if (v < 0 || v >= outDegree.length) throw new IllegalArgumentException("Vertex out of range");
        return outDegree[v];
    }

    public Iterable<Integer> sources() {
        Queue<Integer> sources = new Queue<>();
        for (int v = 0; v < inDegree.length; v++)
            if (inDegree[v] == 0)
                sources.enqueue(v);
        return sources;
    }

    public Iterable<Integer> sinks() {
        Queue<Integer> sinks = new Queue<>();
        for (int v = 0; v < outDegree.length; v++)
            if (outDegree[v] == 0)
                sinks.enqueue(v);
        return sinks;
    }

    public boolean isMap() {
        return isMap;
    }

    public static void main(String[] args) {
        Digraph G = new Digraph(System.in);
        Degrees degrees = new Degrees(G);
        for (int v = 0; v < G.V(); v++) {
            System.out.print("vertex " + v + ": ");
            System.out.println("in-degrees " + degrees.inDegree(v) + " out-degrees " + degrees.outDegree(v));
        }
        System.out.print("Sources: ");
        for (int v : degrees.sources())
            System.out.print(v + " ");
        System.out.println();
        System.out.print("Sinks: ");
        for (int v : degrees.sinks())
            System.out.print(v + " ");
        System.out.println();
    }
}
