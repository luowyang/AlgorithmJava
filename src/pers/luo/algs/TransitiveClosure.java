package pers.luo.algs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TransitiveClosure {
    private DirectedDFS[] all;

    public TransitiveClosure(Digraph G) {
        all = new DirectedDFS[G.V()];
        for (int s = 0; s < G.V(); s++)
            all[s] = new DirectedDFS(G, s); // all[s] stores all vertices reachable from s
    }

    // whether w is reachable from v
    public boolean reachable(int v, int w) {
        return all[v].marked(w);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Digraph G = new Digraph(new FileInputStream(args[0]));
        TransitiveClosure ts = new TransitiveClosure(G);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            System.out.println(w + " is reachable from " + v + ": " + ts.reachable(v, w));
        }
    }
}
