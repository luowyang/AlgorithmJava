package pers.luo.algs;

public class Rational {
    private final long numerator;
    private final long denominator;

    public Rational(int numerator, int denominator)
    {
        if (denominator == 0) throw new RuntimeException("Denominator cannot be zero");
        long r = gcd(numerator, denominator);
        numerator /= r;
        denominator /= r;
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Rational(String rational)
    {
        String[] words = rational.split("/");
        int n = Integer.parseInt(words[0]);
        int d = Integer.parseInt(words[1]);
        if (d == 0) throw new RuntimeException("Denominator cannot be zero");
        long r = gcd(n, d);
        n /= r;
        d /= r;
        if (d < 0) {
            n = -n;
            d = -d;
        }
        numerator = n;
        denominator = d;
    }

    public Rational plus(Rational b)
    {
        long n = numerator * b.denominator + denominator * b.numerator;
        long d = denominator * b.denominator;
        long r = gcd(n, d);
        return new Rational((int)(n/r), (int)(d/r));
    }

    public Rational minus(Rational b)
    {
        long n = numerator * b.denominator - denominator * b.numerator;
        long d = denominator * b.denominator;
        long r = gcd(n, d);
        return new Rational((int)(n/r), (int)(d/r));
    }

    public Rational times(Rational b)
    {
        long n = numerator * b.numerator;
        long d = denominator * b.denominator;
        long r = gcd(n, d);
        return new Rational((int)(n/r), (int)(d/r));
    }

    public Rational divides(Rational b)
    {
        long n = numerator * b.denominator;
        long d = denominator * b.numerator;
        long r = gcd(n, d);
        return new Rational((int)(n/r), (int)(d/r));
    }

    private static long gcd(long p, long q)
    {
        if (Math.abs(p) < Math.abs(q)) {
            long t = p;
            p = q;
            q = t;
        }
        long r;
        while (q != 0) {
            r = p % q;
            p = q;
            q = r;
        }
        return p;
    }

    public boolean equals(Object that)
    {
        if (this == that) return true;
        if (that == null) return false;
        if (this.getClass() != that.getClass()) return false;
        Rational r = (Rational) that;
        if (this.numerator != r.numerator) return false;
        if (this.denominator != r.denominator) return false;
        return true;
    }

    public String toString()
    { return numerator + "/" + denominator; }

    public static void main(String[] args)
    {
        int n = -10;
        int d = 6;
        Rational r1 = new Rational(n, d);
        Rational r2 = new Rational("6/10");
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r1.plus(r2));
        System.out.println(r1.minus(r2));
        System.out.println(r1.times(r2));
        System.out.println(r1.divides(r2));
    }
}
