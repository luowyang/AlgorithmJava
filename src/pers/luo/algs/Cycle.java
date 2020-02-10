package pers.luo.algs;

public class Cycle {
    private boolean[] marked;
    private boolean hasCycle;

    public Cycle(Graph G) {
        marked = new boolean[G.V()];
        // must check cycle in every connected components
        for (int v = 0; v < G.V(); v++)
            if (!marked[v])
                dfs(G, v, v);
    }

    // note that hasCycle dfs has one more vertex parameter than standard version
    // this vertex u is the caller on the edge u-v, where u has already been visited
    // but because of the inherent feature of the adjacent lists,
    // u will be checked again as v's adjacent vertex, this should not be considered to form a cycle
    private void dfs(Graph G, int v, int u) {
        marked[v] = true;
        for (int w : G.adj(v))
            if (!marked[w]) dfs(G, w, v);
            else if (w != u) hasCycle = true;   // if w is not caller u and w has been visited before, G is cyclic
    }

    public  boolean hasCycle() {
        return hasCycle;
    }

    public static void main(String[] args) {
        Graph G = new Graph(System.in);
        Cycle cycle = new Cycle(G);
        System.out.println("has cycle: " + cycle.hasCycle);
    }
}
