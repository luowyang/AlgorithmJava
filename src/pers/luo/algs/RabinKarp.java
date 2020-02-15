package pers.luo.algs;

import java.util.Scanner;

/**
 * Rabin-Karp substring searching algorithm in Monte Carlo approach
 * @author Luo Wenyang
 */
public class RabinKarp {
    private long P;         // first prime
    private long Q;         // second prime
    private int R = 256;    // alphabet size
//    private String pat;
    private int M;
    private long patHashP;
    private long patHashQ;
    private long RMP;       // R^(M-1)%P
    private long RMQ;       // R^(M-1)%Q

    public RabinKarp(String pat) {
        this(pat, "68718952447", "1073676287");
    }

    public RabinKarp(String pat, String P, String Q) {
//        this.pat = pat;
        this.P = Long.parseLong(P);
        this.Q = Long.parseLong(Q);
        M = pat.length();
        // hash R^(M-1)
        RMP = 1;
        for (int i = 0; i < M-1; i++)
            RMP = (R * RMP) % this.P;
        RMQ = 1;
        for (int i = 0; i < M-1; i++)
            RMQ = (R * RMQ) % this.Q;
        patHashP = hash(pat, M, this.P);
        patHashQ = hash(pat, M, this.Q);
    }

    // hashing s[0..M-1] with Horner's method
    private long hash(String s, int M, long P) {
        return hash(s, 0, M, P);
    }

    // hashing s[offset..offset+M-1] with Horner's method
    private long hash(String s, int offset, int M, long P) {
        long h = 0;
        for (int i = 0; i < M; i++)
            h = (h * R + s.charAt(i+offset)) % P;
        return h;
    }

    public int search(String txt) {
        return search(txt, 0);
    }

    public int search(String txt, int offset) {
        int N = txt.length();
        if (N - offset < M) return N;
        // calculate the hash of txt[0..M-1]
        long txtHashP = hash(txt, offset, M, P);
        long txtHashQ = hash(txt, offset, M, Q);
        if (txtHashP == patHashP && txtHashQ == patHashQ) return 0;
        //checking fingerprints
        for (int i = M + offset; i < N; i++) {
            txtHashP = (txtHashP + P - RMP * txt.charAt(i-M) % P) % P;
            txtHashP = (txtHashP * R + txt.charAt(i)) % P;
            txtHashQ = (txtHashQ + Q - RMQ * txt.charAt(i-M) % Q) % Q;
            txtHashQ = (txtHashQ * R + txt.charAt(i)) % Q;
            if (txtHashP == patHashP && txtHashQ == patHashQ) return i - M + 1;   // match by Monte Carlo
        }
        return N;   // no match
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String txt = scanner.nextLine();
            String pat = scanner.nextLine();
            RabinKarp rk = new RabinKarp(pat);
            print(txt, pat, rk.search(txt));
            System.out.println("count: " + rk.count(txt));
            for (int i : rk.searchAll(txt))
                System.out.print(i + " ");
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
