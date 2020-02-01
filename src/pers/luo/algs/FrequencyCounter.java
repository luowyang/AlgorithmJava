package pers.luo.algs;

import javafx.scene.paint.Stop;

import java.util.Scanner;

public class FrequencyCounter {
    private static ST<String, Integer> getST(String alg)
    {
        switch (alg) {
            case "List":
                return new SequentialSearchST<>();
            case "Array":
                return new BinarySearchST<>();
            case "BST":
                return new BST<>();
            case "LLRB":
                return new LLRedBlackBST<>();
            case "TTF":
                return new TTFBST<>();
            case "AVL":
                return new AVLBST<>();
            default:
                throw new IllegalArgumentException("Unknown algorithm " + alg);
        }
    }

    public static double counter(Queue<String> queue, String alg)
    {
        ST<String, Integer> st = getST(alg);
        Queue<String> maxes = new Queue<>();
        int maxCount = 0;
        Stopwatch timer = new Stopwatch();
        for (String word : queue) {
            if (!st.contains(word)) st.put(word, 1);
            else                    st.put(word, st.get(word) + 1);
        }
        // initialize max string
        for (String word : st.keys()) {
            int count = st.get(word);
            if (count > maxCount) {
                maxCount = count;
                while (!maxes.isEmpty()) maxes.dequeue();
                maxes.enqueue(word);
            }
            else if (count == maxCount) {
                maxes.enqueue(word);
            }
        }
        double t = timer.elapsedTime();
        System.out.println(alg);
        for (String max : maxes)
            System.out.println(max + " " + st.get(max));
        System.out.println("time: " + t + " seconds");
        return t;
    }

    public static void main(String[] args)
    {
        String alg = args[0];
        int threshold = Integer.parseInt(args[1]);
        Scanner scanner = new Scanner(System.in);
        Queue<String> queue = new Queue<>();
        Stopwatch timer = new Stopwatch();
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (word.length() >= threshold) queue.enqueue(word);
        }
        System.out.println("Read input in " + timer.elapsedTime() + " seconds");
        counter(queue, alg);
    }
}
