package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

public class Stack<Item> implements Iterable<Item> {
    private Item[] a;   // stack entries
    private int N;      // size

    public Stack(int cap)
    {
        a = (Item[]) new Object[cap];
    }

    public void push(Item item)
    {
        a[N++] = item;
    }

    public Item pop()
    {
        return a[--N];
    }

    public boolean isEmpty()
    {
        return N == 0;
    }

    public int size()
    {
        return N;
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
    }
}
