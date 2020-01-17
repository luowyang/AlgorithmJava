package pers.luo.algs;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class VisualCounter {
    private final int N;
    private final int max;
    private int n;
    private int counter;

    public VisualCounter(int N, int max)
    {
        if (N <= 0) throw new RuntimeException("VisualCounter N non-positive");
        if (max <= 0) throw new RuntimeException("VisualCounter max non-positive");
        this.N = N;
        this.max = max;
        StdDraw.setXscale(0, N+1);
        StdDraw.setYscale(-max-1, max+1);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(0, 0);
    }

    public void increment()
    {
        if (n < N && counter < max) {
            n++;
            counter++;
            StdDraw.point(n, counter);
        }
    }

    public void decrement()
    {
        if (n < N && counter > -max) {
            n++;
            counter--;
            StdDraw.point(n, counter);
        }
    }

    public String toString()
    { return n + "/" + N + " counts: " + counter + " (max: " + max + ")";}

    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[0]);
        int max = Integer.parseInt(args[1]);
        int T = Integer.parseInt(args[2]);
        VisualCounter vc = new VisualCounter(N, max);
        for (int t = 0; t < T; t++) {
            int flag = StdRandom.uniform(0, 2);
            if (flag == 1) vc.increment();
            else           vc.decrement();
        }
        System.out.println(vc);
    }
}
