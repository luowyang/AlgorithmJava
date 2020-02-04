package pers.luo.algs;

import edu.princeton.cs.algs4.In;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class SymbolGraph {
    private SeparateChainingHashST<String, Integer> st;
    private String[] keys;
    private Graph G;

    public SymbolGraph(InputStream in, String delimiter) {
        st = new SeparateChainingHashST<>();
        Scanner scanner = new Scanner(in);
        Queue<String> queue = new Queue<>();    // store input stream
        while (scanner.hasNextLine()) {         // first read
            String line = scanner.nextLine();
            queue.enqueue(line);
            String[] a = line.split(delimiter);
            for (int i = 0; i < a.length; i++)
                if (!st.contains(a[i]))
                    st.put(a[i], st.size());    // link string to index
        }
        // build inverse indexing
        keys = new String[st.size()];
        for (String name: st.keys())
            keys[st.get(name)] = name;
        // second read
        G = new Graph(st.size());
        while (!queue.isEmpty()) {
            String[] a = queue.dequeue().split(delimiter);
            int v = st.get(a[0]);   // get the index of the first vertex of the line
            for (int i = 1; i < a.length; i++)
                G.addEdge(v, st.get(a[i])); // add edges for neighbouring vertices
        }
    }

    public boolean contains(String key) {
        return st.contains(key);
    }

    public int index(String key) {
        return st.get(key);
    }

    public String name(int v) {
        return keys[v];
    }

    // although G is private, client can manipulate G to change the symbol graph
    // this is OK because G only allows adding edges between existing vertices so symbol tables won't need to change
    public Graph G() {
        return G;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = args[0];
        String delimiter = args[1];
        String s = args[2];
        SymbolGraph sg = new SymbolGraph(new FileInputStream(filename), delimiter);

        Graph G = sg.G();
        BreadthFirstPaths bfs = new BreadthFirstPaths(G, sg.index(s));

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String query = scanner.nextLine();
            if (sg.contains(query) && bfs.hasPathTo(sg.index(query))) {
                System.out.println("   (Degrees of Separation: " + bfs.distanceTo(sg.index(query))/2 + ")");
                for (int w : bfs.pathTo(sg.index(query)))
                    System.out.println("   " + sg.name(w));
            }
            else
                System.out.println("   not connected");
        }
    }
}
