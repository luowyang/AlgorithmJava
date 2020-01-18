package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FixedArrayStack<Item> implements Iterable<Item> {
    private Item[] a;    // stack entries
    private int N = 0;                            // size

    public FixedArrayStack(int cap)
    { a = (Item[]) new Object[cap]; }

    public boolean isEmpty()
    { return N == 0; }

    public int size()
    { return N; }

    public void push(Item item)
    {
        if (N == a.length) throw new StackOverflowError("Stack overflow");
        a[N++] = item;
    }

    public Item pop()
    {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = a[--N];
        a[N] = null;    // avoid free object
        return item;
    }

    private class ReverseArrayIterator implements Iterator<Item> {
        private int i = N;  // i indicates which element to be took

        public boolean hasNext() { return i > 0;  }
        public Item next()
        {
            if (!hasNext()) throw new NoSuchElementException("Stack iterator out of bound");
            return a[--i];
        }
    }
    public Iterator<Item> iterator()
    { return new ReverseArrayIterator(); }

    public static void main(String[] args)
    {
        FixedArrayStack<String> stack = new FixedArrayStack<String>(100);
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
