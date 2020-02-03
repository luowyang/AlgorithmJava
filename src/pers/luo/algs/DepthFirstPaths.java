package pers.luo.algs;

/*
 * find all paths starting from vertex s in graph G
 * note that if multiple paths exist for a vertex, only one will be returned
 * which one is chosen relies on graph representation
 */
public class DepthFirstPaths {
    private boolean[] marked;   // visited vertices
    private int[] edgeTo;       // last vertex in the path to w is edgeTo[w]
    private final int s;        // source vertex

    // find all paths from s in G
    public DepthFirstPaths(Graph G, int s) {
        this.s = s;
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        edgeTo[s] = s;
        dfs(G, s);  // DFS pre-process
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;       // set visited mark
        for (int w : G.adj(v))
            if (!marked[w]) {
                edgeTo[w] = v;  // visit w from v
                dfs(G, w);
            }
    }

    // check whether s has a path to v
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    // return the path from s to v, including s and v
    // return null if s and v are disconnected
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;     // this gatekeeper is necessary because edgeTo[] is initialized to 0's
        Stack<Integer> path = new Stack<>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }

    public static void main(String[] args) {
        Graph G = new Graph(System.in);
        int s = Integer.parseInt(args[0]);
        DepthFirstPaths search = new DepthFirstPaths(G, s);
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
