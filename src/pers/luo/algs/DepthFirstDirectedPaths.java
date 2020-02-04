package pers.luo.algs;

public class DepthFirstDirectedPaths {
    private boolean[] marked;
    private int[] edgeTo;
    private int s;

    public DepthFirstDirectedPaths(Digraph G, int s) {
        if (s < 0 || s >= G.V()) throw new IllegalArgumentException("Source out of range");
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        edgeTo[s] = s;
        dfs(G, s);
    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v))
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
    }

    public boolean hasPathTo(int v) {
        if (v < 0 || v >= marked.length) throw new IllegalArgumentException("Vertex out of range");
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }

    public static void main(String[] args) {
        Digraph G = new Digraph(System.in);
        int s = Integer.parseInt(args[0]);
        DepthFirstDirectedPaths search = new DepthFirstDirectedPaths(G, s);
        for (int v = 0; v < G.V(); v++) {
            System.out.print(s + " to " + v + ": ");
            if (search.hasPathTo(v))
                for (int x : search.pathTo(v))
                    if (x == s) System.out.print(x);
                    else        System.out.print("-" + x);
            System.out.println();
        }
    }
}
