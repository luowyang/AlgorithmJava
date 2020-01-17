package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

public class ArrayStack<Item> implements Iterable<Item> {
    private Item[] a = (Item[]) new Object[1];    // stack entries
    private int N = 0;                              // size

    public boolean isEmpty()
    { return N == 0; }

    public int size()
    { return N; }

    private void resize(int max)
    {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++)
            temp[i] = a[i];
        a = temp;
    }

    public void push(Item item)
    {
        if (N == a.length) resize(2*a.length);
        a[N++] = item;
    }

    public Item pop()
    {
        Item item = a[--N];
        a[N] = null;    // avoid free object
        if (N > 0 && N == a.length/4) resize(a.length/2);
        return item;
    }

    private class ReverseArrayIterator implements Iterator<Item> {
        private int i = N;  // i indicates which element to be took

        public boolean hasNext() { return i > 0;  }
        public Item next()       { return a[--i]; }
        public void remove()     {                }
    }

    public Iterator<Item> iterator()
    {
        return new ReverseArrayIterator();
    }

    public static void main(String[] args)
    {
        Stack<String> stack = new Stack<String>(100);
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                stack.push(item);
            else if (!stack.isEmpty())
                System.out.print(stack.pop() + " ");
        }
        System.out.println("(" + stack.size() + " left on stack)");
        System.out.print("remnants on stack:");
        for (String s : stack)
            System.out.print(" " + s);
    }
}
