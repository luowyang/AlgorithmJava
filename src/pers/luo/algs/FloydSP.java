package pers.luo.algs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Floyd All-Pairs Shortest Path Algorithm
 * @author Luo Wenyang
 */
public class FloydSP {
    private double[][] D;       // D[i][j] = D_{i,j,k}
    private Edge[][] next;      // next[i][j] is the next edge in SP_{i,j,k}

    public FloydSP(EdgeWeightedGraph G) {
        // initialize D and next
        D = new double[G.V()][G.V()];
        next = new Edge[G.V()][G.V()];  // null at default
        for (int v = 0; v < G.V(); v++)
            for (int w = 0; w < G.V(); w++)
                D[v][w] = Double.POSITIVE_INFINITY;
        for (int v = 0; v < G.V(); v++)
            for (Edge e : G.adj(v)) {
                int w = e.other(v);
                D[v][w] = e.weight();
                next[v][w] = e;
            }
        for (int v = 0; v < G.V(); v++) {
            D[v][v] = 0.0;
            next[v][v] = new Edge(v, v, 0.0);
        }
        // main body of Floyd algorithm
        for (int k = 0; k < G.V(); k++)
            for (int i = 0; i < G.V(); i++)
                for (int j = 0; j < G.V(); j++)
                    relax(i, j, k);
    }

    // path relaxation, test whether i-->k-->j is shorter than i-->j
    private void relax(int i, int j, int k) {
        if (D[i][j] > D[i][k] + D[k][j]) {  // if i-->k-->j is shorter, than update D[i][j]
            D[i][j] = D[i][k] + D[k][j];
            next[i][j] = next[i][k];
        }
    }

    // length of shortest path from v to w
    public double dist(int v, int w) {
        return D[v][w];
    }

    // whether there is a path from v to w
    public boolean hasPath(int v, int w) {
        return D[v][w] < Double.POSITIVE_INFINITY;
    }

    // path from v to w
    public Iterable<Edge> path(int v, int w) {
        Queue<Edge> path = new Queue<>();
        int x = v;
        while (x != w) {
            Edge e = next[x][w];
            path.enqueue(e);
            x = e.other(x);
        }
        return path;
    }

    public static void main(String[] args) throws FileNotFoundException {
        EdgeWeightedGraph G = new EdgeWeightedGraph(new FileInputStream(args[0]));
        FloydSP sp = new FloydSP(G);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            if (sp.hasPath(v, w)) {
                System.out.printf("%d to %d (%.2f): ", v, w, sp.dist(v, w));
                for (Edge e : sp.path(v, w))
                    System.out.print(e + "  ");
                System.out.println();
            }
            else
                System.out.println("No path from " + v + " to " + w);
        }
    }
}
