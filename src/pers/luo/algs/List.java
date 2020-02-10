package pers.luo.algs;

import java.util.NoSuchElementException;

public class List<Item> {
    private final double init = 0.0;
    private final double offset = 0.1;

    private AVLBST<Double, Item> weight;
    private LinearProbingHashST<Item, Double> lookup;

    public List() {
        weight = new AVLBST<>();
        lookup = new LinearProbingHashST<>();
    }

    public void addFront(Item item) {
        double newWeight = isEmpty() ? init : weight.min();
        newWeight -= offset;
        weight.put(newWeight, item);
        lookup.put(item, newWeight);
    }

    public void addBack(Item item) {
        double newWeight = isEmpty() ? init : weight.max();
        newWeight += offset;
        weight.put(newWeight, item);
        lookup.put(item, newWeight);
    }

    public Item deleteFront() {
        if (isEmpty()) throw new NoSuchElementException("List is empty");
        double minWeight = weight.min();
        Item item = weight.get(minWeight);
        lookup.delete(item);
        weight.delete(minWeight);
        return item;
    }

    public Item deleteBack() {
        if (isEmpty()) throw new NoSuchElementException("List is empty");
        double maxWeight = weight.max();
        Item item = weight.get(maxWeight);
        lookup.delete(item);
        weight.delete(maxWeight);
        return item;
    }

    public void delete(Item item) {
        if (!lookup.contains(item)) return;
        double itemWeight = lookup.get(item);
        weight.delete(itemWeight);
        lookup.delete(item);
    }

    public void add(int index, Item item) {
        if (index < 0 || index >= size()) throw new IllegalArgumentException("Index out of bound");
        if (contains(item)) throw new IllegalArgumentException("Item already exists");
        if (index == 0) {
            addFront(item);
            return;
        }
        if (index == size()) {
            addBack(item);
            return;
        }
        double prevWeight = weight.select(index-1);
        double nextWeight = weight.select(index);
        double newWeight = (prevWeight + nextWeight) / 2.0;
        weight.put(newWeight, item);
        lookup.put(item, newWeight);
    }

    public Item delete(int index) {
        if (index < 0 || index >= size()) throw new IllegalArgumentException("Index out of bound");
        double itemWeight = weight.select(index);
        Item item = weight.get(itemWeight);
        weight.delete(itemWeight);
        lookup.delete(item);
        return item;
    }

    public boolean contains(Item item) {
        return lookup.contains(item);
    }

    public boolean isEmpty() {
        return lookup.isEmpty();
    }

    public int size() {
        return lookup.size();
    }
}
