package pers.luo.algs;

import java.util.Scanner;

public class SymbolGraph {

    public SymbolGraph(String filename, String delimiter) {

    }

    public boolean contain(String key) {

    }

    public int index(String key) {

    }

    public String name(int v) {

    }

    public Graph G() {

    }

    public static void main(String[] args) {
        String filename = args[0];
        String delimiter = args[1];
        SymbolGraph sg = new SymbolGraph(filename, delimiter);

        Graph G = sg.G();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String query = scanner.nextLine();
            for (int w : G.adj(sg.index(query)))
                System.out.println("    " + sg.name(w));
        }
    }
}
