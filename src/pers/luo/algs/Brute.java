package pers.luo.algs;

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

    public int search(String txt) {
        int M = pat.length();
        int N = txt.length();
        for (int i = 0; i <= N - M; i++) {
            int j = 0;
            while (j < M && pat.charAt(j) == txt.charAt(i + j)) j++;
            if (j == M) return i;
        }
        return N;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String txt = scanner.nextLine();
        String pat = scanner.nextLine();
        Brute brute = new Brute(pat);
        print(txt, pat, brute.search(txt));
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
