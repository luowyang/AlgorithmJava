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

    public boolean equals(Object that)
    {
        if (this == that) return true;
        if (that == null) return false;
        if (this.getClass() != that.getClass()) return false;
        Accumulator a = (Accumulator) that;
        if (this.N != a.N) return false;
        if (this.total != a.total) return false;
        return true;
    }

    public static void main(String[] args)
    {
        int T = Integer.parseInt(args[0]);
        Accumulator a = new Accumulator();
        for (int t = 0; t < T; t++)
            a.addDataValue(StdRandom.uniform());
        System.out.println(a);
    }
}
