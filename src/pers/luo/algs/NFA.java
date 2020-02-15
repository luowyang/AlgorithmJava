package pers.luo.algs;

import java.util.Scanner;

/**
 * Regular expression matching using Nondeterministic Finite Automaton
 *
 * Supported regular expression ops:
 * 1. concatenation, or and basic closure
 * 2. wildcard .
 * 3. +
 * 4. {n}, {n,} and {n,m}
 *
 * Some rules for regular expressions:
 * 1. No {,m}
 * 2. No * or + after {}
 *
 * //TODO: add support for [] and [^]
 *
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
        Queue<Integer> metas = new Queue<>();   // used to deal with {n,m}, stores m-n
        parseRE(regex, metas);
        M = re.length;
        G = new Digraph(M + 1);
        compile(metas);
    }

    // parse RE to filter mistakes and convert regex to compilable form
    private void parseRE(String regex, Queue<Integer> metas) {
        StringBuilder reg = new StringBuilder();
        Stack<Integer> symbols = new Stack<>();
        int len = regex.length();
        for (int i = 0; i < len; i++) {
            char c = regex.charAt(i);
            reg.append(c);
            int rp = reg.length();      // 1 pos after right parenthesis or single char
            int lp = rp - 1;            // pos of left parenthesis or single char
            if (c == '(') symbols.push(lp);
            else if (c == ')') lp = symbols.pop();
            if (i < len - 3) {
                char peek = regex.charAt(i + 1);
                if (peek == '{') {          // deal with {} or {,}
                    int beginFirst = (i += 2);
                    int beginSecond;
                    peek = regex.charAt(i);
                    while (peek != ',' && peek != '}'){
                        i++;
                        peek = regex.charAt(i);
                    }
                    int n = Integer.parseInt(regex.substring(beginFirst, i));
                    if (peek == '}') {      // {n}
                        for (int j = 0; j < n - 1; j++)
                            reg.append(reg.substring(lp, rp));
                    }
                    else {                  // {n,} or {n,m}
                        beginSecond = ++i;
                        peek = regex.charAt(i);
                        if (peek == '}') {  // {n,}
                            for (int j = 0; j < n - 1; j++)
                                reg.append(reg.substring(lp, rp));
                            reg.append('+');
                        }
                        else {              // {n,m}
                            while (peek != '}'){
                                i++;
                                peek = regex.charAt(i);
                            }
                            int m = Integer.parseInt(regex.substring(beginSecond, i));
                            for (int j = 0; j < m - 1; j++)
                                reg.append(reg.substring(lp, rp));
                            reg.append('{');
                            metas.enqueue(m - n);               // number of forward links
                        }
                    }
                }
            }
        }
        re =  reg.toString().toCharArray();
    }

    // build NFA with stack
    private void compile(Queue<Integer> metas) {
        Stack<Integer> ops = new Stack<>();
        for (int i = 0; i < M; i++) {
            int lp = i;     // left parenthesis or single char
            if (re[i] == '(' || re[i] == '|')
                ops.push(i);
            else if(re[i] == ')') {
                Bag<Integer> ors = new Bag<>();
                int op;
                for (op = ops.pop(); re[op] == '|'; op = ops.pop())
                    ors.add(op);        // pop all |'s
                lp = op;
                for (int or : ors) {
                    G.addEdge(lp, or + 1);  // add transition from lp to or+1
                    G.addEdge(or, i);           // add transition from or to rp
                }
            }
            if (i < M - 1) {
                // note that lp is ( or single char, depending on whether re[i] is ) or not
                if (re[i + 1] == '*') {
                    // if next is *
                    G.addEdge(i + 1, lp);
                    G.addEdge(lp, i + 1);
                }
                else if (re[i + 1] == '+') {
                    // if next is +
                    G.addEdge(i + 1, lp);
                }
                else if (re[i + 1] == '{') {
                    // if next is {
                    int k = metas.dequeue();    // m-n
                    int skip = i + 1 - lp;
                    for (int j = 0; j < k; j++) {
                        G.addEdge(lp, i + 1);
                        lp = lp - skip;
                    }
                }
            }
            addEpsilonTransitionToNextState(i);
        }
    }

    private void addEpsilonTransitionToNextState(int i) {
        if (re[i] == '(' || re[i] == ')' || re[i] == '*' || re[i] == '+'
            || re[i] == '{')
            G.addEdge(i, i+1);          // add epsilon transition to next state for these ops
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
        String pat = "(" + args[0] + ")";
        NFA nfa = new NFA(pat);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String txt = scanner.nextLine();
            if (nfa.recognizes(txt))
                System.out.println(txt);
        }
    }
}
