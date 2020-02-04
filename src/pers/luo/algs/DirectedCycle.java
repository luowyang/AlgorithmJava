package pers.luo.algs;

public class DirectedCycle {
    private boolean[] marked;
    private boolean[] onStack;      // used to check whether a vertex is on recursion stack
    private int[] edgeTo;
    private Stack<Integer> cycle;   // stack is used to store vertices on a directed cycle

    public DirectedCycle(Digraph G) {
        marked = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo = new int[G.V()];
        for (int s = 0; s < G.V(); s++)
            if (!marked[s]) dfs(G, s);  // look for directed cycle in every connected components
    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        onStack[v] = true;      // begin recursion call so push v into stack
        for (int w : G.adj(v))
            if (hasCycle()) break;      // if already has a cycle, finish work
            else if (!marked[w]) {      // deal with unvisited vertex
                edgeTo[w] = v;
                dfs(G, w);
            }
            else if (onStack[w]) {      // deal with vertices on stack, which indicate a cycle
                cycle = new Stack<>();  // initial cycle
                for (int x = v; x != w; x = edgeTo[x])
                    cycle.push(x);      // collect all vertices on stack from v to w
                cycle.push(w);          // collect w as beginning
                cycle.push(v);          // collect v again to form a closed cycle
            }
        onStack[v] = false; // finish recursion call so clear v from stack
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Iterable<Integer> cycle() {
        return cycle;
    }

    public static void main(String[] args) {
        Digraph G = new Digraph(System.in);
        DirectedCycle cycle = new DirectedCycle(G);
        int s = -1;
        for (int v : cycle.cycle()) {
            if (s < 0) {
                s = v;
                System.out.print(v + "-");
            }
            else if (s == v)
                System.out.print(v);
            else
                System.out.print(v + "-");
        }
        System.out.println();
    }
}
