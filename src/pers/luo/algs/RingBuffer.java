package pers.luo.algs;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RingBuffer<Item> implements Iterable<Item> {
    private Item[] buffer;
    private int N;
    private int first;
    private int last;

    public RingBuffer(int size)
    {
        buffer = (Item[]) new Object[size];
    }

    public boolean isEmpty()
    { return N == 0; }
    public boolean isFull()
    { return N == buffer.length; }
    public int size()
    { return N; }

    private int getIndex(int index)
    {
        // if (index < 0) return (index + buffer.length) % buffer.length;
        // if (index >= buffer.length) return index % buffer.length;
        //return index;
        return index % buffer.length;
    }

    public void produce(Item item)
    {
        if (isFull()) throw new RuntimeException("Buffer overflow");
        buffer[last] = item;
        last = getIndex(last++);
        N++;
    }

    public Item consume()
    {
        if (isEmpty()) throw new NoSuchElementException("Buffer underflow");
        Item item = buffer[first];
        first = getIndex(first++);
        N--;
        return item;
    }

    private class ArrayIterator implements Iterator<Item> {
        private int count = 0;

        public boolean hasNext()
        { return count < N;  }
        public Item next()
        { return buffer[getIndex(first+(count++))]; }
    }
    public Iterator<Item> iterator()
    { return new ArrayIterator(); }
}
