package pers.luo.algs;

/*
* order DAG vertices with depth first algorithm
*/
public class DepthFirstOrder {
    private boolean[] marked;
    private Queue<Integer> pre;
    private Queue<Integer> post;
    private Stack<Integer> reversePost;

    public DepthFirstOrder(Digraph G) {
        marked = new boolean[G.V()];
        pre         = new Queue<>();
        post        = new Queue<>();
        reversePost = new Stack<>();
        for (int s = 0; s < G.V(); s++)
            if (!marked[s]) dfs(G, s);
    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        pre.enqueue(v);         // order of recursive call
        for (int w : G.adj(v))
            if (!marked[w]) dfs(G, w);
        post.enqueue(v);        // order of return
        reversePost.push(v);    // order of reverse post order
    }

    public Iterable<Integer> pre() {
        return pre;
    }

    public Iterable<Integer> post() {
        return post;
    }

    public Iterable<Integer> reversePost() {
        return reversePost;
    }

    public static void main(String[] args) {
        Digraph G = new Digraph(System.in);
        DepthFirstOrder dfs = new DepthFirstOrder(G);

        System.out.println("pre-order:");
        for (int v : dfs.pre())
            System.out.print(v + " ");
        System.out.println();
        System.out.println("post-order:");
        for (int v : dfs.post())
            System.out.print(v + " ");
        System.out.println();
        System.out.println("reverse-post-order:");
        for (int v : dfs.reversePost())
            System.out.print(v + " ");
        System.out.println();

    }
}
