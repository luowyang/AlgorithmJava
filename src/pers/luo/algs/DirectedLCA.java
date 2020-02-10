package pers.luo.algs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
* find Lowest Common Ancestor in DAG with BFS
*/
public class DirectedLCA {
    private Digraph R;

    public DirectedLCA(Digraph G) {
        DirectedCycle cycleFinder = new DirectedCycle(G);
        if (cycleFinder.hasCycle()) throw new IllegalArgumentException("DirectedLAC argument must be a DAG");
        R = G.reverse();
    }

    public int find(int v, int w) {
        boolean[] ancV = new boolean[R.V()];
        boolean[] ancW = new boolean[R.V()];
        Queue<Integer> queueV = new Queue<>();
        Queue<Integer> queueW = new Queue<>();
        queueV.enqueue(v);
        ancV[v] = true;
        queueW.enqueue(w);
        ancW[w] = true;
        while (!queueV.isEmpty() || !queueW.isEmpty()) {
            if (!queueV.isEmpty()) {
                int x = queueV.dequeue();
                for (int y : R.adj(x))
                    if      (ancW[y] ) return y;
                    else if (!ancV[y]) {
                        ancV[y] = true;
                        queueV.enqueue(y);
                    }
            }
            if (!queueW.isEmpty()) {
                int x = queueW.dequeue();
                for (int y : R.adj(x))
                    if      (ancV[y] ) return y;
                    else if (!ancW[y]) {
                        ancW[y] = true;
                        queueW.enqueue(y);
                    }
            }
        }
        return -1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Digraph G = new Digraph(new FileInputStream(args[0]));
        DirectedLCA lca = new DirectedLCA(G);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            int LCA = lca.find(v, w);
            System.out.println("  LCA: " + (LCA<0?"NA":LCA));
        }
    }
}
