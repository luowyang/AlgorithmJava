package pers.luo.algs;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class VisualAccumulator {
    private int N;
    private double total;

    public VisualAccumulator(int trials, double max)
    {
        StdDraw.setXscale(0, trials);
        StdDraw.setYscale(0, max);
        StdDraw.setPenRadius(.005);
    }

    public void addDataValue(double val)
    {
        N++;
        total += val;
        StdDraw.setPenColor(StdDraw.DARK_GRAY);
        StdDraw.point(N, val);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.point(N, total/N);
    }

    public double mean()
    { return total/N; }

    public String toString()
    { return "Mean (" + N + " values) " + String.format("%7.5f", mean()); }

    public boolean equals(Object that)
    {
        if (this == that) return true;
        if (that == null) return false;
        if (this.getClass() != that.getClass()) return false;
        VisualAccumulator va = (VisualAccumulator) that;
        if (this.N != va.N) return false;
        if (this.total != va.total) return false;
        return true;
    }

    public static void main(String[] args)
    {
        int T = Integer.parseInt(args[0]);
        VisualAccumulator va = new VisualAccumulator(T, 1.0);
        for (int t = 0; t < T; t++)
            va.addDataValue(StdRandom.uniform());
        System.out.println(va);
    }
}
