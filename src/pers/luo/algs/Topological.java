package pers.luo.algs;

/*
* topological order of DAG
*/
public class Topological {
    private Iterable<Integer> order;    // topological order

    public Topological(Digraph G) {
        DirectedCycle cycleFinder = new DirectedCycle(G);
        if (!cycleFinder.hasCycle()) {  // topological order is only possible for DAG
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.reversePost();  // topological order is exactly reverse post order
        }
    }

    public Topological(EdgeWeightedDigraph G) {
        DirectedCycle cycleFinder = new DirectedCycle(G);
        if (!cycleFinder.hasCycle()) {  // topological order is only possible for DAG
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.reversePost();  // topological order is exactly reverse post order
        }
    }

    // whether G is Directed Acyclic Graph
    public boolean isDAG() {
        return order != null;
    }

    // vertices in topological order
    public Iterable<Integer> order() {
        return order;
    }

    public static void main(String[] args) {
        SymbolDigraph sg = new SymbolDigraph(System.in, args[0]);
        Topological top = new Topological(sg.G());
        for (int v : top.order())
            System.out.println(sg.name(v));
    }
}
