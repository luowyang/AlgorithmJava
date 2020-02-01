package pers.luo.algs;

import java.util.Scanner;

public class SearchCompare {
    public static void main(String[] args)
    {
        int threshold = Integer.parseInt(args[2]);
        Scanner scanner = new Scanner(System.in);
        Queue<String> queue = new Queue<>();
        Stopwatch timer = new Stopwatch();
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (word.length() >= threshold) queue.enqueue(word);
        }
        System.out.println("Read input in " + timer.elapsedTime() + " seconds");
        double t1 = FrequencyCounter.counter(queue, args[0]);
        double t2 = FrequencyCounter.counter(queue, args[1]);
        System.out.println(args[0] + " is " + t2/t1 + " times faster than " + args[1]);
    }
}
