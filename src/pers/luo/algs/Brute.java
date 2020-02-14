package pers.luo.algs;

import edu.princeton.cs.algs4.In;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Brute force substring searching algorithms
 * @author Luo Wenyang
 */
public class Brute {
    private String pat;

    private static class CharBuffer {
        char[] buffer;
        int size;       // size of buffer
        int first;      // first char in buffer
        int last;       // 1 char after the last char in buffer, position to add a new char
        public CharBuffer(int size) {
            this.size = size + 1;
            buffer = new char[this.size];
        }
        public char read(int i) {
            return buffer[(first + i) % size];
        }
        public void add(char c) {
            buffer[last++] = c;
            last %= size;
        }
        public void remove() {
            buffer[first++] = 0;
            first %= size;
        }
    }

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

    public Iterable<Integer> search(InputStream in) {
        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("");
        Queue<Integer> queue = new Queue<>();
        int M = pat.length();
        int i = 0;
        CharBuffer buffer = new CharBuffer(M);
        for (int j = 0; j < M-1 && scanner.hasNext(); j++)  // j<M-1 because in[M-1] will be read in next loop block
            buffer.add(scanner.next().charAt(0));
        while (scanner.hasNext()) {
            buffer.add(scanner.next().charAt(0));
            int j = 0;
            while (j < M && pat.charAt(j) == buffer.read(j)) j++;
            if (j == M) queue.enqueue(i);
            buffer.remove();
            i++;
        }
        return queue;
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
        Brute brute = new Brute("ABABAC");
        for (int i : brute.search(System.in)) {
            System.out.print(i + " ");
        }
        /*Scanner scanner = new Scanner(System.in);
        String txt = scanner.nextLine();
        String pat = scanner.nextLine();
        Brute brute = new Brute(pat);
        print(txt, pat, brute.search(txt));
        System.out.println("count: " + brute.count(txt));
        for (int i : brute.searchAll(txt))
            System.out.print(i + " ");*/
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
