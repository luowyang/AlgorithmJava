package pers.luo.algs;

public class DirectedDFS {
    private boolean[] marked;

    public DirectedDFS(Digraph G, int s) {
        if (s < 0 || s >= G.V()) throw new IllegalArgumentException("Source out of range");
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    public DirectedDFS(Digraph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        for (int s : sources)
            if (!marked[s]) dfs(G, s);  // check every unmarked source
    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v))
            if (!marked[w]) dfs(G, w);  // check every unmarked reachable neighbour
    }

    public boolean marked(int v) {
        if (v < 0 || v >= marked.length) throw new IllegalArgumentException("Vertex out of range");
        return marked[v];
    }

    public static void main(String[] args) {
        Digraph G = new Digraph(System.in);

        Bag<Integer> bag = new Bag<>();
        for (String s : args)
            bag.add(Integer.parseInt(s));

        DirectedDFS dfs = new DirectedDFS(G, bag);

        for (int v = 0; v < G.V(); v++)
            if (dfs.marked[v]) System.out.print(v + " ");
        System.out.println();
    }
}
