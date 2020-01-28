package pers.luo.algs;

import javafx.scene.paint.Stop;

import java.util.Scanner;

public class FrequencyCounter {
    private static ST<String, Integer> getST(String type)
    {
        if (type.equals("List"))
            return new SequentialSearchST<>();
        else if (type.equals("Array"))
            return new BinarySearchST<>();
        return new SequentialSearchST<>();
    }

    public static void main(String[] args)
    {
        String type = args[0];
        int threshold = Integer.parseInt(args[1]);
        ST<String, Integer> st = getST(type);
        Scanner scanner = new Scanner(System.in);
        Stopwatch timer = new Stopwatch();
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (word.length() < threshold) continue;
            if (!st.contains(word)) st.put(word, 1);
            else                    st.put(word, st.get(word) + 1);
        }
        // initialize max string
        String max = " ";
        st.put(max, 0);
        for (String word : st.keys())
            if (st.get(word) > st.get(max))
                max = word;
        double t = timer.elapsedTime();
        System.out.println(max + " " + st.get(max));
        System.out.println("time: " + t + " seconds");
    }
}
