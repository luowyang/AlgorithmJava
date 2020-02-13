package pers.luo.algs;


import java.util.Scanner;

/**
 * Substring searching algorithms
 * List: Brute Force, KMP
 * @author Luo Wenyang
 */
public class SubstringSearch {
    /**
     * Non-return version of brute force searching
     * @param txt text string
     * @param pat pattern string
     * @return    index of the first matching char if matched, otherwise length of txt
     */
    public static int brute(String txt, String pat) {
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
        print(txt, pat, brute(txt, pat));
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
