package pers.luo.algs;

import java.awt.*;

/**
 * Abstract data structure for an interval
 *
 * @author Luo Wenyang
 */
public class Interval implements Comparable<Interval> {
    private final double low, high;

    public Interval(double low, double high) {
        if (low > high) throw new IllegalArgumentException("Invalid interval endpoints");
        this.low = low;
        this.high = high;
    }

    public boolean overlaps(Interval that) {
        return this.low <= that.high && this.high >= that.low;
    }

    public double length() {
        return high - low;
    }

    public double low() {
        return low;
    }

    public double high() {
        return high;
    }

    public int compareTo(Interval that) {
        if      (this.low < that.low) return -1;
        else if (this.low > that.low) return +1;
        else if (this.high < that.high) return -1;
        else if (this.high > that.high) return +1;
        return 0;
    }

    public String toString() {
        return "[" + low + ", " + high + "]";
    }
}
