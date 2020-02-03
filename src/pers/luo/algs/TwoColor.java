package pers.luo.algs;

public class TwoColor {
    private boolean[] marked;
    private boolean[] color;
    private boolean isBipartite = true;

    public TwoColor(Graph G) {
        marked = new boolean[G.V()];
        color = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v])
                dfs(G, v);  // default color is false
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v))
            if (!marked[w]) {   // if uncolored, color w with the opposite color
                color[w] = !color[v];
                dfs(G, w);
            }
            else if (color[w] == color[v]) isBipartite = false; // w is colored with the same color, uncolorable
    }

    public boolean isBipartite() {
        return isBipartite;
    }

    public static void main(String[] args) {
        Graph G = new Graph(System.in);
        TwoColor twoColor = new TwoColor(G);
        System.out.println("is two colorable: " + twoColor.isBipartite());
    }
}
