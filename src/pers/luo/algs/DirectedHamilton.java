package pers.luo.algs;

import java.util.Iterator;

/*
* find directed hamiltonian path
* algorithm: in topological order, check whether an edge exists between every two neighbours
* if true, then the hamilton path is exactly the topological order (and edges between neighbours)
* if false, then the hamilton paths does not exist
*/
public class DirectedHamilton {
    private Iterable<Integer> hamilton;

    public DirectedHamilton(Digraph G) {
        DepthFirstOrder dfs = new DepthFirstOrder(G);
        Iterable<Integer> order = dfs.reversePost();
        Iterator<Integer> iterator = order.iterator();
        int v, w;
        if (iterator.hasNext()) v = iterator.next();
        else                    return;
        while (iterator.hasNext()) {
            w = iterator.next();
            if (!G.hasEDge(v, w)) return;
            v = w;
        }
        hamilton = order;
    }

    public boolean hasHamilton() {
        return hamilton != null;
    }

    public Iterable<Integer> hamiltonPath() {
        return hamilton;
    }

    public static void main(String[] args) {
        Digraph G = new Digraph(System.in);
        DirectedHamilton h = new DirectedHamilton(G);
        System.out.println("Hamilton path exists: " + h.hasHamilton());
        if (!h.hasHamilton()) return;
        Iterator<Integer> iterator = h.hamiltonPath().iterator();
        if (iterator.hasNext()) System.out.print(iterator.next());
        while (iterator.hasNext())
            System.out.println(iterator.next() + "-");
        System.out.println();
    }
}
