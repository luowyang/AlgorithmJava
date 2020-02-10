package pers.luo.algs;

import java.util.Scanner;

/**
 * Critical Path Method for solving priority constrained parallel scheduling problem
 * @author Luo Wenyang
 */
public class CPM {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = Integer.parseInt(scanner.nextLine());   // warning: the \n behind int will NOT be skipped by next()
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(2*N+2); // 2 vertices for each job, a source and a sink
        int s = 2 * N, t = 2 * N + 1;           // last two indexes are source and sink
        // build graph
        for (int i = 0; i < N; i++) {
            String[] a = scanner.nextLine().split("\\s+");
            double duration = Double.parseDouble(a[0]);
            G.addEdge(s, i, 0.0);       // start vertex is i
            G.addEdge(i, i+N, duration);    // end vertex is i+N
            G.addEdge(i+N, t, 0.0);
            for (int j = 1; j < a.length; j++)  // add edge between priority constrained jobs
                G.addEdge(i+N, Integer.parseInt(a[j]), 0.0);
        }
        // find and print longest paths
        System.out.println("Start times:");
        AcyclicLP lp = new AcyclicLP(G, s);
        for (int i = 0; i < N; i++)
            System.out.printf("%4d  %5.1f\n", i, lp.distTo(i));
        System.out.println("Finish: " + lp.distTo(t));
    }
}
