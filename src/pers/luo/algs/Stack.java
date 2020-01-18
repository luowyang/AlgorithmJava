package pers.luo.algs;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Stack<Item> implements Iterable<Item> {
    private Node top;   // stack top entry
    private int N;      // size
    private class Node
    {   // embedded Node class for linked list
        Item item;
        Node next;

        public Node(Item item, Node next)
        {
            this.item = item;
            this.next = next;
        }
    }

    public boolean isEmpty()
    {
        return top == null;
    }   // or N == 0

    public int size()
    {
        return N;
    }

    public void push(Item item)
    {   // add item to stack top
        top = new Node(item, top);
        N++;
    }

    public Item pop()
    {   // delete and return top item
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = top.item;
        top = top.next;
        N--;
        return item;
    }

    private class ListIterator implements Iterator<Item> {
        private Node current= top;  // current indicates which element to be took

        public boolean hasNext()
        { return current != null; }
        public Item next()
        {
            if(!hasNext()) throw new NoSuchElementException("Stack iterator out of bound");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    public Iterator<Item> iterator()
    {
        return new ListIterator();
    }

    public static void main(String[] args)
    {
        Stack<String> stack = new Stack<String>();
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
