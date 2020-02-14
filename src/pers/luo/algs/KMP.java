package pers.luo.algs;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Knuth-Morris-Pratt substring searching algorithm
 * @author Luo Wenyang
 */
public class KMP {
    private String pat;     // pattern string
    private int[][] dfa;    // state-table of DFA
    private int reboot;

    private int R = 256;

    public KMP(String pat) {
        this.pat = pat;
        // build DFA
        buildDFA();
    }

    private void buildDFA() {
        int X, j, M = pat.length();
        dfa = new int[R][M];
        // set dfa[][0]
        dfa[pat.charAt(0)][0] = 1;
        // set other dfa[][], do not start with j=0 or X will be set to wrong value for j=1
        for (X = 0, j = 1; j < M; j++) {
            // calculate dfa when mismatched
            for (char c = 0; c < R; c++)
                dfa[c][j] = dfa[c][X];
            // calculate dfa when matched
            dfa[pat.charAt(j)][j] = j + 1;
            // update X for j + 1
            X = dfa[pat.charAt(j)][X];
        }
        reboot = X;
    }

    public void setPattern(String pat) {
        this.pat = pat;
        buildDFA();
    }

    // simulate DFA
    public int search(String txt) {
        return search(txt, 0);
    }

    public int search(String txt, int offset) {
        int N = txt.length();
        int M = pat.length();
        int i, j;   // i is the text pointer, j is the pattern pointer and the current state
        for (i = offset, j = 0; i < N && j < M; i++)
            j = dfa[txt.charAt(i)][j];
        return j == M ? i - M : N;
    }

    public Iterable<Integer> search(InputStream in) {
        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("");
        Queue<Integer> queue = new Queue<>();
        int M = pat.length();
        int i = 0, j = 0;   // j is the pattern pointer and the current state
        while (scanner.hasNext()) {
            String ch = scanner.next();
            j = dfa[ch.charAt(0)][j];
            i++;
            if (j == M) {
                queue.enqueue(i - M);
                j = reboot;
            }
        }
        return queue;
    }

    public int count(String txt) {
        int n = 0, i = 0, N = txt.length();
        while (true) {
            i = search(txt, i);
            if (i == N) break;
            n++;
            i++;
        }
        return n;
    }

    public Iterable<Integer> searchAll(String txt) {
        Queue<Integer> queue = new Queue<>();
        int i = 0, N = txt.length();
        while (true) {
            i = search(txt, i);
            if (i == N) break;
            queue.enqueue(i);
            i++;
        }
        return queue;
    }

    public String toString() {
        int M = pat.length();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < M; j++)
                s.append(dfa[i][j]).append(" ");
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
       KMP kmp = new KMP("ABABAC");
       for (int i : kmp.search(System.in)) {
           System.out.print(i + " ");
       }
        /*Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String txt = scanner.nextLine();
            String pat = scanner.nextLine();
            KMP kmp = new KMP(pat);
            print(txt, pat, kmp.search(txt));
            System.out.println("count: " + kmp.count(txt));
            for (int i : kmp.searchAll(txt))
                System.out.print(i + " ");
        }*/
    }

    // print text and pattern in a distinguished format
    private static void print(String txt, String pat, int i) {
        System.out.println(i == txt.length() ? "No match" : "match");
        System.out.print("text   : ");
        for (char c : txt.toCharArray())
            System.out.print(c + " ");
        System.out.println();
        System.out.print("pattern: ");
        i %= txt.length();
        for (int j = 0; j < i; j++)
            System.out.print("  ");
        for (char c : pat.toCharArray())
            System.out.print(c + " ");
        System.out.println();
    }
}
