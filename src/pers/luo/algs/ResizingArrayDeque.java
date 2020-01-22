package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ResizingArrayDeque<Item> implements Iterable<Item> {
    private Item[] a = (Item[]) new Object[1];
    private int left;
    private int right;
    private int N;

    public boolean isEmpty()
    { return N == 0; }

    public boolean isFull()
    { return N == a.length; }

    public int size()
    { return N; }

    private int getIndex(int index)
    {
        if (index < 0) return (index + a.length) % a.length;
        if (index >= a.length) return index % a.length;
        return index;
    }

    private void resize(int max)
    {
        if (max < N) throw new RuntimeException("Deque overflow");
        Item[] tmp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++)
            tmp[i] = a[getIndex(left + 1 + i)];
        a = tmp;
        left = a.length - 1;
        right = getIndex(N);
    }

    public void pushLeft(Item item)
    {
        if (isFull()) resize(2 * N);
        a[left] = item;
        left = getIndex(--left);
        N++;
    }

    public void pushRight(Item item)
    {
        if (isFull()) resize(2 * N);
        a[right] = item;
        right = getIndex(++right);
        N++;
    }

    public Item popLeft()
    {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        if (N == a.length/4) resize(a.length / 2);
        left = getIndex(++left);
        Item item = a[left];
        a[left] = null;
        N--;
        return item;
    }

    public Item popRight()
    {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        if (N == a.length/4) resize(a.length / 2);
        right = getIndex(--right);
        Item item = a[right];
        a[right] = null;
        N--;
        return item;
    }

    private class DoublyLinkedListIterator implements Iterator<Item> {
        private int count = 0;  // current indicates which element to be took

        public boolean hasNext()
        { return count < N; }
        public Item next()
        {
            if(!hasNext()) throw new NoSuchElementException("Deque iterator out of bound");
            return a[getIndex(left + 1 + (count++))];
        }
    }
    public Iterator<Item> iterator()
    {
        return new DoublyLinkedListIterator();
    }

    public static void main(String[] args)
    {
        ResizingArrayDeque<String> deque = new ResizingArrayDeque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (item.equals("-"))
                deque.pushLeft(item);
            else
                deque.pushRight(item);
        }
        System.out.println("(" + deque.size() + " left on deque)");
        System.out.print("remnants on deque:");
        for (String s : deque)
            System.out.print(" " + s);
        System.out.println("");
        while (deque.popLeft().equals("-"));
        System.out.print("remnants on deque:");
        for (String s : deque)
            System.out.print(" " + s);
        System.out.println("");
        while (!deque.isEmpty())
            System.out.print(deque.popRight() + " ");
        System.out.println("");
    }
}
