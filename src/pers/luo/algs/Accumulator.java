package pers.luo.algs;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Stream processing
 * Advantages: real-time processing, less calculations, avoiding rounding error
 */
public class Accumulator {
    private int N;
    //private double total;
    private double m;
    private double s;

    public void addDataValue(double val)
    {
        N++;
        s = s + 1.0*(N-1)/N*(val-m)*(val-m);
        m = m + (val-m)/N;
    }

    public double mean()
    { return m; }

    public double var()
    { return s/(N-1); }

    public double stddev()
    { return Math.sqrt(this.var()); }

    public String toString()
    { return N + " values Mean " + String.format("%7.5f", mean()) + " Variance " + String.format("%7.5f", var()); }

    public boolean equals(Object that)
    {
        if (this == that) return true;
        if (that == null) return false;
        if (this.getClass() != that.getClass()) return false;
        Accumulator a = (Accumulator) that;
        if (this.N != a.N) return false;
        if (this.m != a.m) return false;
        if (this.s != a.s) return false;
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
