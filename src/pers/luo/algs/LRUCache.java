package pers.luo.algs;

import java.util.Scanner;

public class LRUCache<Item> {
    private LinearProbingHashST<Item, Node<Item>> set;
    private Node<Item> first, last;

    private static class Node<Item> {
        Item key;
        Node<Item> prev, next;
        public Node(Item key, Node<Item> prev, Node<Item> next) {
            this.key = key;
            this.prev = prev;
            this.next = next;
        }
    }

    public LRUCache(int capacity) {
        set = new LinearProbingHashST<>(capacity);
    }
    public LRUCache() {
        this(997);
    }

    public void access(Item key) {
        if (!set.contains(key)) {
            first = new Node<>(key, null, first);
            set.put(key, first);
            if (first.next != null) first.next.prev = first;
            if (last == null) last = first;
        }
        else {
            Node<Item> node = set.get(key);
            if (first == node) return;
            if (last == node) last = node.prev;
            node.prev.next = node.next;
            if (node.next != null) node.next.prev = node.prev;
            node.prev = null;
            node.next = first;
            first.prev = node;
            first = node;
        }
    }

    public void delete() {
        set.delete(last.key);
        last = last.prev;
        if (last == null) first = null;
        else              last.next = null;
    }

    public int size() {
        return set.size();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public static void main(String[] args) {
        LRUCache<String> cache = new LRUCache<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.next();
            cache.access(s);
        }
        for (String s : cache.set.keys())
            System.out.print(s + " ");
        System.out.println();
        cache.delete();
        cache.access("R");
        cache.delete();
        for (String s : cache.set.keys())
            System.out.print(s + " ");
        System.out.println();
    }
}
