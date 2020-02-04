package pers.luo.algs;

public class BreadthFirstDirectedPaths {
    private boolean[] marked;
    private int[] edgeTo;
    private int[] distTo;
    private int s;

    public BreadthFirstDirectedPaths(Digraph G, int s) {
        if (s < 0 || s >= G.V()) throw new IllegalArgumentException("Source out of range");
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        this.s = s;
        bfs(G, s);
    }

    private void bfs(Digraph G, int s) {
        Queue<Integer> queue = new Queue<>();
        marked[s] = true;
        edgeTo[s] = s;
        queue.enqueue(s);
        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            for (int w : G.adj(v))
                if (!marked[w]) {
                    marked[w] = true;   // prevent multiple enqueuing
                    edgeTo[w] = v;
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
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }

    public int distTo(int v) {
        if (v < 0 || v >= marked.length) throw new IllegalArgumentException("Vertex out of range");
        if (!hasPathTo(v)) return -1;
        return distTo[v];
    }

    public static void main(String[] args) {
        Digraph G = new Digraph(System.in);
        int s = Integer.parseInt(args[0]);
        BreadthFirstDirectedPaths search = new BreadthFirstDirectedPaths(G, s);
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
