package pers.luo.algs;

/*
 * find all shortest paths starting from vertex s in graph G
 * note that if multiple shortest paths exist for a vertex, only one will be returned
 * which one is chosen relies on graph representation
 */
public class BreadthFirstPaths {
    private boolean[] marked;
    private int[] edgeTo;
    private int[] distTo;
    private final int s;

    public BreadthFirstPaths(Graph G, int s) {
        this.s = s;
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        bfs(G, s);
    }

    private void bfs(Graph G, int s) {
        Queue<Integer> queue = new Queue<>();
        edgeTo[s] = s;
        queue.enqueue(s);
        marked[s] = true;
        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            for (int w : G.adj(v))
                if (!marked[w]) {
                    marked[w] = true;   // prevent multiple enqueuing
                    edgeTo[w] = v;      // set last vertex of shortest path to w to be v
                    distTo[w] = distTo[v] + 1;
                    queue.enqueue(w);
                }
        }
    }

    public boolean hasPathTo(int v) {
        if (v < 0 || v >= marked.length) throw new IllegalArgumentException("Vertex out of range");
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;     // this gatekeeper is necessary because edgeTo[] is initialized to 0's
        Stack<Integer> path = new Stack<>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }

    public int distTo(int v) {        // calculate the length of shortest path
        if (!hasPathTo(v)) return -1;     // this gatekeeper is necessary because edgeTo[] is initialized to 0's
        return distTo[v];
    }

    public static void main(String[] args) {
        Graph G = new Graph(System.in);
        int s = Integer.parseInt(args[0]);
        BreadthFirstPaths search = new BreadthFirstPaths(G, s);
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
