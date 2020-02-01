package pers.luo.algs;

public class Vector<Item extends Comparable<Item>> implements Comparable<Vector<Item>> {
    private final int dim;
    private final Item[] vector;

    public Vector(int dim)
    {
        this.dim = dim;
        vector = (Item[]) new Comparable[dim];
    }

    public Vector(Item[] array)
    {
        dim = array.length;
        vector = (Item[]) new Comparable[dim];
        for (int i = 0; i < dim; i++)
            vector[i] = array[i];
    }

    public int compareTo(Vector<Item> that)
    {
        for (int i = 0; i < dim; i++) {
            int cmp = vector[i].compareTo(that.vector[i]);
            if (cmp != 0) return cmp;
        }
        return 0;
    }
}
