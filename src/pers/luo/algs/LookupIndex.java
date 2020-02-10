package pers.luo.algs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LookupIndex {
    private static int[] parseArgs(String[] args) {
        int[] indexes = new int[args.length-3];
        for (int i = 0; i < indexes.length; i++)
            indexes[i] = Integer.parseInt(args[i+3]);
        return indexes;
    }

    private static ST<String, Queue<String>>[] buildIndex(String indexFile, String delimiter, int keyCol) throws FileNotFoundException {
        LinearProbingHashST<String, Queue<String>> sts[] = (LinearProbingHashST<String, Queue<String>>[]) new LinearProbingHashST[2];
        sts[0] = new LinearProbingHashST<>();
        sts[1] = new LinearProbingHashST<>();
        Scanner scanner = new Scanner(new File(indexFile));
        while (scanner.hasNextLine()) {
            String[] ss = scanner.nextLine().split(delimiter);
            String key = ss[keyCol];
            for (int i = 0; i < ss.length; i++) {
                if (i == keyCol) continue;
                String val = ss[i].replaceAll("\"", "");
                if (val.equals("")) continue;
                if (!sts[0].contains(key.toLowerCase())) sts[0].put(key.toLowerCase(), new Queue<>());
                if (!sts[1].contains(val.toLowerCase())) sts[1].put(val.toLowerCase(), new Queue<>());
                sts[0].get(key.toLowerCase()).enqueue(val);
                sts[1].get(val.toLowerCase()).enqueue(key);
            }
        }
        return sts;
    }

    public static void main(String[] args) throws FileNotFoundException {
        ST<String, Queue<String>>[] sts = buildIndex(args[0], args[1], Integer.parseInt(args[2]));
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String word = scanner.nextLine().toLowerCase();
            if (sts[0].contains(word))
                for (String s : sts[0].get(word))
                    System.out.println(" " + s);
            if (sts[1].contains(word))
                for (String s : sts[1].get(word))
                    System.out.println(" " + s);
        }
    }
}
