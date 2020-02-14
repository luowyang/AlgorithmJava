package pers.luo.algs;

import edu.princeton.cs.algs4.In;

import java.net.SecureCacheResponse;
import java.util.Scanner;

/**
 * Regular expression matching using Nondeterministic Finite Automaton
 * @author Luo Wenyang
 */
public class NFA {
    private char[] re;      // regex and also match transitions
    private Digraph G;      // epsilon transitions
    private int M;          // # of states

    private static class DFS {
        boolean[] marked;
        Bag<Integer> bag;
        public DFS(Digraph G, int s) {
            marked = new boolean[G.V()];
            bag = new Bag<>();
            dfs(G, s);
        }
        public DFS(Digraph G, Iterable<Integer> sources) {
            marked = new boolean[G.V()];
            bag = new Bag<>();
            for (int s : sources)
                if (!marked[s]) dfs(G, s);
        }
        private void dfs(Digraph G, int v) {
            marked[v] = true;
            bag.add(v);
            for (int w : G.adj(v))
                if (!marked[w]) dfs(G, w);
        }
        public Iterable<Integer> collect() {
            return bag;
        }
    }

    public NFA(String regex) {
        re = regex.toCharArray();
        M = regex.length();
        G = new Digraph(M + 1);
        buildNFA();
    }

    // build NFA with stack
    private void buildNFA() {
        Stack<Integer> ops = new Stack<>();
        for (int i = 0; i < M; i++) {
            int lp = i;     // left parenthesis or single char
            if (re[i] == '(' || re[i] == '|')
                ops.push(i);
            else if(re[i] == ')') {
                int or = ops.pop();     // top op, may be | or (
                if (re[or] == '|') {    // if |
                    lp = ops.pop();     // lp is now (
                    G.addEdge(lp, or + 1);
                    G.addEdge(or, i);
                }
                else {                  // if (
                    lp = or;            // lp is now (
                }
            }
            if (i < M-1 && re[i+1] == '*') {    // if next is *
                // note that lp is ( or single char
                G.addEdge(i+1, lp);
                G.addEdge(lp, i+1);
            }
            if (re[i] == '(' || re[i] == '*' || re[i] == ')')
                G.addEdge(i, i+1);          // add epsilon transition to next state for these ops
        }
    }

    // simulate NFA
    public boolean recognizes(String txt) {
        Bag<Integer> pc = new Bag<>();  // possible states
        DFS dfs = new DFS(G, 0);     // DFS to find reachable states
        // find states reachable from 0
        for (int j : dfs.collect())
            pc.add(j);
        // run NFA
        int N = txt.length();
        for (int i = 0; i < N; i++) {
            char c = txt.charAt(i);             // read a char
            Bag<Integer> match = new Bag<>();   // matched states in possible states
            for (int j : pc) {
                if (j == M)     // accept state
                    match.add(j);
                else if (re[j] == c || re[j] == '.')
                    match.add(j + 1);       // match transitions
            }
            pc = new Bag<>();               // throw all obsolete possible states
            dfs = new DFS(G, match);        // epsilon transitions
            for (int j : dfs.collect())     // update possible states
                pc.add(j);
        }
        for (int j : pc)
            if (j == M) return true;
        return false;
    }

    public static void main(String[] args) {
        String pat = "(.*" + args[0] + ".*)";
        NFA nfa = new NFA(pat);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String txt = scanner.nextLine();
            if (nfa.recognizes(txt))
                System.out.println(txt);
        }
    }
}
