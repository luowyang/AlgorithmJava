package pers.luo.algs;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Palindrome checking algorithm in Monte Carlo approach based on fingerprint
 * @author Luo Wenyang
 */
public class Palindrome {
    private long R = 256;
    private long P;
    private long Q;

    public Palindrome() {
        this("68718952447", "1073676287");
    }

    public Palindrome(String P, String Q) {
        this.P = Long.parseLong(P);
        this.Q = Long.parseLong(Q);
    }

    public boolean isPalindrome(String txt) {
        long leftHashP = txt.charAt(0) % P;
        long rightHashP = txt.charAt(0) % P;
        long RKP = R % P;
        long leftHashQ = txt.charAt(0) % Q;
        long rightHashQ = txt.charAt(0) % Q;
        long RKQ = R % Q;
        int N = txt.length();
        for (int i = 1; i < N; i++) {
            char c = txt.charAt(i);
            leftHashP = (leftHashP * R + c) % P;
            rightHashP = (rightHashP + (c * RKP) % P) % P;
            RKP = (RKP * R) % P;
            leftHashQ = (leftHashQ * R + c) % Q;
            rightHashQ = (rightHashQ + (c * RKQ) % Q) % Q;
            RKQ = (RKQ * R) % Q;
        }
        return (leftHashP == rightHashP) && (leftHashQ == rightHashQ);
    }

    public static void main(String[] args) {
        int T = Integer.parseInt(args[0]);
        for (int i = 0; i < T; i++) {
            String s = generateString(21);
            Palindrome p = new Palindrome();
            System.out.println(s);
            System.out.println("is palindrome: " + p.isPalindrome(s));
        }
    }

    private static String generateString(int N) {
        if (StdRandom.uniform(2) == 0) return generatePalindrome(N);
        return generateRandomString(N);
    }

    private static String generatePalindrome(int N) {
        int half = N / 2;
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < half; i++)
            s.append((char) StdRandom.uniform('A', 'Z'+1));
        if (N % 2 == 1) s.append((char) StdRandom.uniform('A', 'Z'+1));
        for (int i = 1; i <= half; i++)
            s.append(s.charAt(half - i));
        return s.toString();
    }

    private static String generateRandomString(int N) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < N; i++)
            s.append((char) StdRandom.uniform('A', 'Z'+1));
        return s.toString();
    }
}