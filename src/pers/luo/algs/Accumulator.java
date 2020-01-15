package pers.luo.algs;

import edu.princeton.cs.algs4.StdRandom;

public class Accumulator {
    private int N;
    private double total;
    public void addDataValue(double val)
    { N++; total += val; }
    public double mean()
    { return total/N; }
    public String toString()
    { return "Mean (" + N + " values): " + String.format("%7.5f", mean()); }

    public static void main(String[] args)
    {
        int T = Integer.parseInt(args[0]);
        Accumulator a = new Accumulator();
        for (int t = 0; t < T; t++)
            a.addDataValue(StdRandom.uniform());
        System.out.println(a);
    }
}
