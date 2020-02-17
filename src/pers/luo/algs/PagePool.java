package pers.luo.algs;

import javax.print.DocFlavor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Unused page pool for B-tree implementation of the ordered symbol table
 *
 * Implementation: circular doubly-linked list with sentinel
 *
 * @author Luo Wenyang
 */
public class PagePool implements java.io.Serializable {
    private static class Node {
        int id;
        Node prev, next;
        public Node(int id, Node prev, Node next) {
            this.id   = id;
            this.prev = prev;
            this.next = next;
        }
    }

    private transient Node sent;
    private transient int size;

    public PagePool() {
        this(9999);
    }

    public PagePool(int cap) {
        sent = new Node(-1, null, null);
        sent.prev = sent;
        sent.next = sent;
        for (int i = 1; i <= cap; i++)
            insert(i);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int fetch() {
        if (size == 0) throw new RuntimeException("No spare id");
        Node node = sent.next;
        remove(node);
        return node.id;
    }

    public void release(int id) {
        insert(id);
    }

    private void insert(int id) {
        sent.prev = new Node(id, sent.prev, sent);
        sent.prev.prev.next = sent.prev;
        size++;
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
    }

    /**
     * Serialization methods
     */

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.write(size);
        for (Node node = sent.next; node != sent; node = node.next)
            out.write(node.id);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int size = in.readInt();
        sent = new Node(-1, null, null);
        sent.prev = sent;
        sent.next = sent;
        for (int i = 0; i < size; i++)
            insert(in.readInt());
    }
}
