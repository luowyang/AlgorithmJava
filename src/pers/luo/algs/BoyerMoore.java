package pers.luo.algs;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Boyer-Moore substring searching algorithm
 * @author Luo Wenyang
 */
public class BoyerMoore {
    private String pat;
    private int[] right;    // jump table

    private int R = 256;

    public BoyerMoore(String pat) {
        this.pat = pat;
        process();
    }

    private void process() {
        int M = pat.length();
        right = new int[R];
        Arrays.fill(right, -1);
        for (int j = 0; j < M; j++)
            right[pat.charAt(j)] = j;   // last index of pattern's char
    }

    public void patter(String pat) {
        this.pat = pat;
        process();
    }

    public int search(String txt) {
        int N = txt.length();
        int M = pat.length();
        int skip;   // skip distance of i
        for (int i = 0; i <= N - M; i += skip) {
            skip = 0;   // reset skip to 0
            for (int j = M - 1; j >= 0; j--)
                if (txt.charAt(i+j) != pat.charAt(j)) { // mismatched
                    skip = j - right[txt.charAt(i+j)];  // calculate skip, including cases 1 and 2b
                    if (skip < 1) skip = 1;     // case 2b
                    break;
                }
            if (skip == 0) return i;    // match success
        }
        return N;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String txt = scanner.nextLine();
            String pat = scanner.nextLine();
            BoyerMoore bm = new BoyerMoore(pat);
            print(txt, pat, bm.search(txt));
        }
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
