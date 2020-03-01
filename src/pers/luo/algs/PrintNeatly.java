package pers.luo.algs;

import java.io.OutputStream;
import java.util.Scanner;

/**
 * Neatly print a bag of words
 *
 * @author Luo Wenyang
 */
public class PrintNeatly {
    public static void print(String[] words, int M) {
        int N = words.length;
        int[] length = getLength(words);
        int[] cost = new int[N + 1];    // cost of the print scheme from k to N-1
        int[] last = new int[N];        // last word of the first line of the print scheme from k to N-1
        for (int k = N-1; k >= 0; k--) {
            cost[k] = Integer.MAX_VALUE;
            int sum = length[k];    // sum is the length of the current line
            if (sum > M) throw new IllegalArgumentException("M is to small to contain the longest word");
            int j = k;
            while (j < N && sum <= M) {
                int c = M - sum + cost[j+1];
                if (c < cost[k]) {
                    cost[k] = c;
                    last[k] = j;
                }
                sum += 1 + length[++j];
            }
        }
        print(words, last, M);
        System.out.println("expected cost: " + cost[0]);
    }

    private static int[] getLength(String[] words) {
        int[] length = new int[words.length + 1];
        for (int i = 0; i < words.length; i++)
            length[i] = words[i].length();
        return length;
    }

    private static void print(String[] words, int[] last, int M) {
        int begin = 0;      // begin of the current line
        int cost = 0;
        while (begin < words.length) {
            int end = last[begin];    // end of the current line
            int sum = words[begin].length();
            System.out.print(words[begin]);
            for (int j = begin+1; j <= end; j++) {
                System.out.print(" " + words[j]);
                sum += 1 + words[j].length();
            }
            System.out.println();
            if (sum > M) throw new RuntimeException("Fail to generate valid print scheme");
            cost += M - sum;
            begin = end + 1;
        }
        System.out.println("actual cost: " + cost);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Queue<String> queue = new Queue<>();
        while (scanner.hasNext()) {
            queue.enqueue(scanner.next());
        }
        String[] input = new String[queue.size()];
        int i = 0;
        for (String s : queue) {
            input[i++] = s;
        }
        int M = Integer.parseInt(args[0]);
        print(input, M);
    }
}
