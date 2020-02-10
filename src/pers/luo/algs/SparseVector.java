package pers.luo.algs;

public class SparseVector {
    private SeparateChainingHashST<Integer, Double> st;
    private final double PRECISION = 1e-20;

    public SparseVector() {
        this(null);
    }

    public SparseVector(double[] x) {
        st = new SeparateChainingHashST<>();
        if (x == null) return;
        for (int i = 0; i < x.length; i++)
            if (Math.abs(x[i]) >= PRECISION) st.put(i, x[i]);
    }

    public int size() {
        return st.size();
    }

    public void put(int i, double x) {
        st.put(i, x);
    }

    public double get(int i) {
        if (!st.contains(i)) return 0.0;
        return st.get(i);
    }

    public double dot(double[] that) {
        double sum = 0.0;
        for (int i : st.keys())
            sum += that[i] * this.get(i);
        return sum;
    }

    public SparseVector sum(SparseVector that) {
        //SparseVector sum = new SparseVector();
        for (int k : that.st.keys()) {
            double a = st.contains(k) ? st.get(k) : 0.0;
            double b = that.st.get(k);
            double c = a + b;
            if (c >= PRECISION) st.put(k, c);
            else                st.delete(k);
        }
        return this;
    }
}
