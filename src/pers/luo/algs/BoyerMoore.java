package pers.luo.algs;

import edu.princeton.cs.algs4.In;

import java.io.InputStream;
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
        return search(txt, 0);
    }

    public int search(String txt, int offset) {
        int N = txt.length();
        int M = pat.length();
        int skip;   // skip distance of i
        for (int i = offset; i <= N - M; i += skip) {
            skip = 0;   // reset skip to 0
            for (int j = M - 1; j >= 0; j--) {
                if (txt.charAt(i + j) != pat.charAt(j)) { // mismatched
                    skip = j - right[txt.charAt(i + j)];  // calculate skip, including cases 1 and 2b
                    if (skip < 1) skip = 1;     // case 2b
                    break;
                }
            }
            if (skip == 0) return i;    // match success
        }
        return N;
    }

    public Iterable<Integer> search(InputStream in) {
        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("");
        Queue<Integer> queue = new Queue<>();
        int M = pat.length();
        CharBuffer buffer = new CharBuffer(M);
        int i = 0, skip = M;    // skip distance is also the number of chars to read into buffer
        while (scanner.hasNext()) {
            int k;
            for (k = 0; k < skip && scanner.hasNext(); k++)
                buffer.add(scanner.next().charAt(0));
            if (k < skip) break;
            skip = 0;
            for (int j = M-1; j >= 0; j--) {
                if (buffer.read(j) != pat.charAt(j)) {
                    skip = j - right[buffer.read(j)];
                    if (skip < 1) skip = 1;
                    break;
                }
            }
            if (skip == 0) {
                queue.enqueue(i);
                skip = 1;           // if matched, i need to skip 1 char
            }
            for (int j = 0; j < skip; j++)
                buffer.remove();
            i += skip;
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

    public static void main(String[] args) {
        BoyerMoore bm = new BoyerMoore("it is a far far better thing that i do than i have ever done");
        for (int i : bm.search(System.in)) {
            System.out.print(i + " ");
        }
        /*Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String txt = scanner.nextLine();
            String pat = scanner.nextLine();
            BoyerMoore bm = new BoyerMoore(pat);
            print(txt, pat, bm.search(txt));
            System.out.println("count: " + bm.count(txt));
            for (int i : bm.searchAll(txt))
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
