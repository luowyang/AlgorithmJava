package pers.luo.algs;

public class SparseVector {
    private SeparateChainingHashST<Integer, Double> st;

    public SparseVector() {
        this(null);
    }

    public SparseVector(double[] x) {
        st = new SeparateChainingHashST<>();
        if (x == null) return;
        for (int i = 0; i < x.length; i++)
            if (x[i] != 0.0) st.put(i, x[i]);
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
        
    }
}
