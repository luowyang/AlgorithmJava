package pers.luo.algs;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Graham's scan algorithm
 *
 * @author Luo Wenyang
 */
public class GrahamScan {
    private static class Point implements Comparable<Point> {
        final double x;
        final double y;
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        public int compareTo(Point that) {
            double c = (that.x * this.y) - (this.x * that.y);
            if (c > 0.0) return +1;
            if (c < 0.0) return -1;
            return Double.compare(this.y, that.y);
        }
    }

    private double[] x;
    private double[] y;

    public GrahamScan(double[] x, double[] y) {
        if (x.length != y.length) throw new IllegalArgumentException("Inconsistent points");
        int ref = 0, n = x.length;
        if (n < 3) throw new IllegalArgumentException("Less than 3 points");
        for (int i = 0; i < n; i++) {
            if (y[i] < y[ref] || (y[i] == y[ref] && x[i] < x[ref]))
                ref = i;
        }
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++)
            points[i] = new Point(x[i] - x[ref], y[i] - y[ref]);
        Quick.sort(points);
        Stack<Point> stack = new Stack<>();
        stack.push(points[0]);
        stack.push(points[1]);
        stack.push(points[2]);
        for (int i = 3; i < n; i++) {
            while (turnRight(stack.peekSecond(), stack.peek(), points[i]))
                stack.pop();
            stack.push(points[i]);
        }
        this.x = new double[stack.size()];
        this.y = new double[stack.size()];
        for (int i = 0; i < this.x.length; i++) {
            Point p = stack.pop();
            this.x[i] = p.x + x[ref];
            this.y[i] = p.y + y[ref];
        }
    }

    private boolean turnRight(Point p1, Point p2, Point p3) {
        double x1 = p2.x - p1.x;
        double y1 = p2.y - p1.y;
        double x2 = p3.x - p2.x;
        double y2 = p3.y - p2.y;
        double prod = (x2 * y1) - (x1 * y2);
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
        GrahamScan ch = new GrahamScan(x, y);
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
