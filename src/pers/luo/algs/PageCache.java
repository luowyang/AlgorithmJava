package pers.luo.algs;

/**
 * Abstract data type of a pool of items for B tree
 * Can be used for recording both undistributed ids and pages in memory
 *
 * Implementation: circular doubly-linked list with sentinel + hashtable
 * Support move-to-front strategy
 */
public class PageCache<Key extends Comparable<Key>, Value> {
    private static class Node<Key extends Comparable<Key>, Value> {
        Page<Key, Value> page;
        Node<Key, Value> prev, next;
        public Node(Page<Key, Value> page, Node<Key, Value> prev, Node<Key, Value> next) {
            this.page = page;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<Key, Value> sent;
    private SeparateChainingHashST<Page<Key, Value>, Node<Key, Value>> st;
    private int size;

    public PageCache() {
        this(997);
    }

    public PageCache(int cap) {
        sent = new Node<>(null, null, null);
        sent.prev = sent;
        sent.next = sent;
        st = new SeparateChainingHashST<>(cap);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void visit(Page<Key, Value> page) {
        if (st.contains(page))
            moveToFront(st.get(page));
        else
            insert(page);
        //System.out.println(String.format("(DEBUG: page %d is visited)", page.getId()));
    }

    private void insert(Page<Key, Value> page) {
        sent.next = new Node<>(page, sent, sent.next);
        sent.next.next.prev = sent.next;
        st.put(page, sent.next);
        size++;
    }

    private void moveToFront(Node<Key, Value> node) {
        if (node == null) throw new RuntimeException("Node is null");
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.prev = sent;
        node.next = sent.next;
        sent.next = node;
        node.next.prev = node;
    }

    public Page<Key, Value> removeLRU() {
        if (size == 0) throw new RuntimeException("Page cache is empty");
        Node<Key, Value> node = sent.prev;
        remove(node);
        return node.page;
    }

    public void remove(Page<Key, Value> page) {
        if (!st.contains(page)) return;
        remove(st.get(page));
        //System.out.println(String.format("(DEBUG: page %d is removed)", page.getId()));
    }

    private void remove(Node<Key, Value> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
    }

    public void printPages() {
        for (Node<Key, Value> node = sent.next; node != sent; node = node.next)
            System.out.println(node.page.getId());
    }

}
