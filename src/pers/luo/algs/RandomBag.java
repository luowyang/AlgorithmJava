package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomBag<Item> implements Iterable<Item> {
    private Item[] a = (Item[]) new Object[1];
    private int N;

    public boolean isEmpty()
    { return N == 0; }

    public boolean isFull()
    { return N == a.length; }

    public int size()
    { return N; }

    private void resize(int max)
    {
        if (max < N) throw new RuntimeException("Bag underflow");
        Item[] tmp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++)
            tmp[i] = a[i];
        a = tmp;
    }

    public void add(Item item)
    {   // add item to the bag
        if (isFull()) resize(2 * N);
        a[N++] = item;
    }

    private void swap(int x, int y)
    {
        Item tmp = a[x];
        a[x] = a[y];
        a[y] = tmp;
        tmp = null;
    }

    private class RandomArrayIterator implements Iterator<Item> {
        private int count = N;

        public boolean hasNext()
        { return count > 0; }
        public Item next()
        {
            if(!hasNext()) throw new NoSuchElementException("Bag iterator out of bound");
            swap(count - 1, StdRandom.uniform(count));
            return a[--count];
        }
    }
    public Iterator<Item> iterator()
    { return new RandomArrayIterator();}

    public static void main(String[] args)
    {
        RandomBag<String> bag = new RandomBag<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) bag.add(item);
        }
        System.out.print("remnants on random bag:");
        for (String s : bag)
            System.out.print(" " + s);
    }
}