package pers.luo.algs;

import edu.princeton.cs.algs4.In;

import java.util.Scanner;

/**
 * Brute force substring searching algorithms
 * @author Luo Wenyang
 */
public class Brute {
    private String pat;

    public Brute(String pat) {
        this.pat = pat;
    }

    public void pattern(String pat) {
        this.pat = pat;
    }

    public int search(String txt) {
        return search(txt, 0);
    }

    public int search(String txt, int offset) {
        int M = pat.length();
        int N = txt.length();
        for (int i = offset; i <= N - M; i++) {
            int j = 0;
            while (j < M && pat.charAt(j) == txt.charAt(i + j)) j++;
            if (j == M) return i;
        }
        return N;
    }

    public int count(String txt) {
        int n = 0, i = 0, N = txt.length();
        while (true) {
            i = search(txt, i);
            if (i == N) break;
            i++;
            n++;
        }
        return n;
    }

    public Iterable<Integer> searchAll(String txt) {
        int i = 0, N = txt.length();
        Queue<Integer> queue = new Queue<>();
        while (true) {
            i = search(txt, i);
            if (i == N) break;
            queue.enqueue(i);
            i++;
        }
        return queue;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String txt = scanner.nextLine();
        String pat = scanner.nextLine();
        Brute brute = new Brute(pat);
        print(txt, pat, brute.search(txt));
        System.out.println("count: " + brute.count(txt));
        for (int i : brute.searchAll(txt))
            System.out.print(i + " ");
    }

    // print text and pattern in a distinguished format
    private static void print(String txt, String pat, int i) {
        System.out.println(i == txt.length() ? "No match" : "match");
        for (char c : txt.toCharArray())
            System.out.print(c + " ");
        System.out.println();
        i %= txt.length();
        for (int j = 0; j < i; j++)
            System.out.print("  ");
        for (char c : pat.toCharArray())
            System.out.print(c + " ");
        System.out.println();
    }
}
