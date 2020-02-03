package pers.luo.algs;

/*
* analyse connected components of a graph
*/
public class CC {
    private boolean[] marked;
    private int[] id;
    private int count;  // current cc number, also total number of cc's

    public CC(Graph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        id[v] = count;
        for (int w : G.adj(v))
            if (!marked[w])
                dfs(G, w);
    }

    public boolean connected(int v, int w) {
        return id[v] == id[w];
    }

    public int count() {
        return count;
    }

    // id is between 0 and count()-1
    public int id(int v) {
        return id[v];
    }

    public static void main(String[] args) {
        Graph G = new Graph(System.in);
        CC cc = new CC(G);
        int M = cc.count();
        System.out.println(M + " components");
        Bag<Integer>[] components;
        components = (Bag<Integer>[]) new Bag[M];
        for (int i = 0; i < M ;i++)
            components[i] = new Bag<>();
        for (int v = 0; v < G.V(); v++)
            components[cc.id(v)].add(v);
        for (int i = 0; i < M; i++) {
            for (int v : components[i])
                System.out.print(v + " ");
            System.out.println();
        }
    }
}
