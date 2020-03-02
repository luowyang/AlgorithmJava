package pers.luo.algs;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Gift wrapping algorithm for finding convex hull
 *
 * @author Luo Wenyang
 */
public class GiftWrapping {
    private static class Point implements Comparable<Point> {
        final double x;
        final double y;
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        public int compareTo(Point that) {
            int cmp = Double.compare(this.y, that.y);
            return cmp == 0.0 ? Double.compare(this.x, that.x) : cmp;
        }
    }

    private double[] x;
    private double[] y;

    public GiftWrapping(double[] x, double[] y) {
        if (x.length != y.length) throw new IllegalArgumentException("Inconsistent points");
        int n = x.length;
        if (n < 3) throw new IllegalArgumentException("Less than 3 points");
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++)
            points[i] = new Point(x[i], y[i]);
        Quick.sort(points);
        Queue<Point> queue = new Queue<>();
        // build right chain
        int k = 0;
        while (k < n - 1) {
            queue.enqueue(points[k]);
            int min = k + 1;
            for (int i = min+1; i < n; i++) {
                if (less(points[i], points[min], points[k]))
                    min = i;
            }
            k = min;
        }
        // build left chain
        k = n - 1;
        while (k > 0) {
            queue.enqueue(points[k]);
            int min = k - 1;
            for (int i = min-1; i >= 0; i--) {
                if (less(points[i], points[min], points[k]))
                    min = i;
            }
            k = min;
        }
        int m = queue.size();
        this.x = new double[m];
        this.y = new double[m];
        for (int i = 0; i < m; i++) {
            Point p = queue.dequeue();
            this.x[i] = p.x;
            this.y[i] = p.y;
        }
    }

    private boolean less(Point p1, Point p2, Point ref) {
        double x1 = p1.x - ref.x;
        double y1 = p1.y - ref.y;
        double x2 = p2.x - ref.x;
        double y2 = p2.y - ref.y;
        double prod = (x1 * y2) - (x2 * y1);
        return prod >= 0.0;
    }

    public double[] x() {
        return x;
    }

    public double[] y() {
        return y;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        double[] x = new double[n];
        double[] y = new double[n];
        double xMin = Double.parseDouble(args[1]);
        double xMax = Double.parseDouble(args[2]);
        double yMin = Double.parseDouble(args[3]);
        double yMax = Double.parseDouble(args[4]);
        for (int i = 0; i < n; i++) {
            x[i] = StdRandom.uniform(xMin, xMax);
            y[i] = StdRandom.uniform(yMin, yMax);
        }
        StdDraw.setXscale(xMin - 0.5, xMax + 0.5);
        StdDraw.setYscale(yMin - 0.5, yMax + 0.5);
        drawPoints(x, y);
        GiftWrapping ch = new GiftWrapping(x, y);
        drawConvex(ch.x(), ch.y());
    }

    private static void drawPoints(double[] x, double[] y) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (int i = 0; i < x.length; i++)
            StdDraw.point(x[i], y[i]);
    }

    private static void drawConvex(double[] x, double[] y) {
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.01);
        for (int i = 0; i < x.length; i++)
            StdDraw.point(x[i], y[i]);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.002);
        for (int i = 0; i < x.length - 1; i++)
            StdDraw.line(x[i], y[i], x[i+1], y[i+1]);
        StdDraw.line(x[0], y[0], x[x.length-1], y[y.length-1]);
    }
}
