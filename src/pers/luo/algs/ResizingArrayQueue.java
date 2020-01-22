package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

public class ResizingArrayQueue<Item> implements Iterable<Item> {
    private Item[] a = (Item[]) new Object[1];  // queue entries
    private int first = 0;                      // first end
    private int last = 0;                       // last end
    private int N = 0;                          // size

    public boolean isEmpty()
    { return N == 0; }

    public int size()
    { return N; }

    private void resize(int max)
    {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++)
            temp[i] = a[(first+i)%a.length];
        a = temp;
        first = 0;
        last = N % a.length;
    }

    public void enqueue(Item item)
    {
        if (N == a.length) resize(2*a.length);
        a[last] = item;
        last = (last + 1) % a.length;
        N++;
    }

    public Item dequeue()
    {
        Item item = a[first];
        a[first] = null;    // avoid free object
        first = (first + 1) % a.length;
        N--;
        if (N > 0 && N == a.length/4) resize(a.length/2);
        return item;
    }

    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;  // i indicates which element to be took

        public boolean hasNext()
        { return i < N;  }
        public Item next()
        { return a[(first+(i++))%a.length]; }
    }
    public Iterator<Item> iterator()
    { return new ArrayIterator(); }

    public static void main(String[] args)
    {
        ResizingArrayQueue<String> queue = new ResizingArrayQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                queue.enqueue(item);
            else if (!queue.isEmpty())
                System.out.print(queue.dequeue() + " ");
        }
        System.out.println("(" + queue.size() + " left on queue)");
        System.out.print("remnants on queue:");
        for (String s : queue)
            System.out.print(" " + s);
    }
}