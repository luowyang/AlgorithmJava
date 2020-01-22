package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

public class QuickFindUF {
    private int[] id;
    private int count;

    public QuickFindUF(int N)
    {
        id = new int[N];
        count = N;
        for (int i = 0; i < N; i++)
            id[i] = i;
    }

    public int count()
    { return count; }

    public boolean connected(int p, int q)
    { return find(p) == find(q); }

    public int find(int p)
    { return id[p]; }

    public void union(int p, int q)
    {
        if (p < 0 || p >= id.length || q < 0 || q >= id.length)
            throw new RuntimeException("Site number out of bound");
        int pID = find(p);
        int qID = find(q);
        if (pID == qID) return;
        for (int i = 0; i < id.length; i++) {
            if (id[i] == pID) id[i] = qID;
        }
        count--;
    }

    public static void main(String[] args)
    {
        int N = StdIn.readInt();
        QuickFindUF uf = new QuickFindUF(N);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.connected(p, q)) continue;
            uf.union(p, q);
            System.out.println(p + " " + q);
        }
        System.out.println(uf.count() + "components");
    }
}
