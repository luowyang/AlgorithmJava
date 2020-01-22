package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomQueue<Item> implements Iterable<Item> {
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
        if (max < N) throw new RuntimeException("Queue underflow");
        Item[] tmp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++)
            tmp[i] = a[i];
        a = tmp;
    }

    public void enqueue(Item item)
    {
        if (isFull()) resize(2 * N);
        a[N++] = item;
    }

    public Item dequeue()
    {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        swap(N - 1, StdRandom.uniform(N));
        Item item = a[--N];
        a[N] = null;
        if (N > 0 && N == a.length/4) resize(a.length/2);
        return item;
    }

    private Item sample()
    { return a[StdRandom.uniform(N)]; }

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
        RandomQueue<String> queue = new RandomQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                queue.enqueue(item);
        }
        System.out.println("(" + queue.size() + " left on queue)");
        System.out.print("samples on queue:");
        for (int i = 0; i < queue.size(); i++)
            System.out.print(" " + queue.sample());
        System.out.println("");
        while (!queue.isEmpty())
            System.out.print(queue.dequeue() + " ");
        System.out.println("");
    }
}