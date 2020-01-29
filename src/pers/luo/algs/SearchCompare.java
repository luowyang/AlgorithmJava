package pers.luo.algs;

import java.util.Scanner;

public class SearchCompare {
    public static void main(String[] args)
    {
        int threshold = Integer.parseInt(args[2]);
        Scanner scanner = new Scanner(System.in);
        Queue<String> queue = new Queue<>();
        while (scanner.hasNext()) {
            String word = scanner.next();
            queue.enqueue(word);
        }
        double t1 = FrequencyCounter.counter(queue, args[0], threshold);
        double t2 = FrequencyCounter.counter(queue, args[1], threshold);
        System.out.println(args[0] + " is " + t2/t1 + " times faster than " + args[1]);
    }
}
