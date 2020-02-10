package pers.luo.algs;

/*
* Kosaraju algorithm for finding strongly connected components
*/
public class KosarajuSCC {
    private boolean[] marked;
    private int[] id;           // id[v] is the identifier of SCC to which v belongs
    private int count;          // counter of SCCs, also identifier of SCCs when doing dfs

    public KosarajuSCC(Digraph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        DepthFirstOrder order = new DepthFirstOrder(G.reverse());
        for (int s : order.reversePost())
            if (!marked[s]) {
                dfs(G, s);
                count++;
            }
    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        id[v] = count;      // visited vertices in the same dfs series belong to the same SCC
        for (int w : G.adj(v))
            if (!marked[w]) dfs(G, w);
    }

    public boolean stronglyConnected(int v, int w) {
        if (v < 0 || v >= marked.length) throw new IllegalArgumentException("First vertex out of range");
        if (w < 0 || w >= marked.length) throw new IllegalArgumentException("Second vertex out of range");
        return id[v] == id[w];
    }

    // number of SCCs
    public int count() {
        return count;
    }

    // identifier of v's SCC
    public int id(int v) {
        if (v < 0 || v >= marked.length) throw new IllegalArgumentException("Vertex out of range");
        return id[v];
    }

    public static void main(String[] args) {
        Digraph G = new Digraph(System.in);
        KosarajuSCC scc = new KosarajuSCC(G);
        for (int i = 0; i < scc.count; i++) {
            System.out.println("SCC id " + i + ":");
            for (int v = 0; v < G.V(); v++)
                if (scc.id[v] == i)
                    System.out.print(v + " ");
            System.out.println();
        }
    }
}
