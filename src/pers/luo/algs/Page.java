package pers.luo.algs;

import java.io.*;

/**
 * Page of B-tree for ordered symbol table implementation
 * @param <Key>
 * @param <Value>
 *
 * @author Luo Wenyang
 */
@SuppressWarnings("unchecked")
public class Page<Key extends Comparable<Key>, Value> implements java.io.Serializable {
    private static final int M = 500;  // node capacity
    private boolean isLeaf;             // true if external, false if internal
    private AVLBST<Key, Link> keys;     // binary search tree to store keys
    private String id;                  // id of this page
    private static String path = "./data/BTree/Page";

    private transient int n;     // only used to split nodes

    private class Link implements java.io.Serializable {
        String id;
        transient Page<Key, Value> next;
        Value value;
        int size;                   // number of keys in the subtree pointed by the link
        public Link(String id, Page<Key, Value> next, Value value, int size) {
            this.id = id;           // id of the next page, null if the link contains a value
            this.next = next;       // pointer to the next page, null if no next or unloaded
            this.value = value;     // value is null for internal links
            this.size = size;
        }
        // check if the page pointed by the link has been loaded
        // if the link contains a value, it will always return true
        public boolean isOpened() {
            return id == null || next != null;
        }
        // open the next page pointed by this link
        // if not already opened, this method will load the page from disk
        public Page<Key, Value> open() throws IOException, ClassNotFoundException {
            if (isOpened()) return next;
            FileInputStream fileIn = new FileInputStream(getFilename(id));
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Page<Key, Value> page = (Page<Key, Value>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Page " + page.id + " has been loaded");
            next = page;
            page.n = size;
            return page;
        }
    }

    // create and open a page
    public Page(String id, boolean bottom) {
        this.id = id;
        isLeaf = bottom;
        keys = new AVLBST<>();
    }

    // close and write a page back
    public void close() throws IOException {
        // This method will put the page on disk
        FileOutputStream fileOut = new FileOutputStream(getFilename(id));
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
        fileOut.close();
    }

    // add a key to this external page, this method will increment this page's size
    // return whether a new key has been added
    public boolean add(Key key, Value value) {
        if (!isLeaf) throw new UnsupportedOperationException("Cannot add keys to an internal node");
        if (!keys.contains(key)) {
            keys.put(key, new Link(null, null, value, 1));
            n++;
            return true;
        }
        keys.get(key).value = value;
        return false;
    }

    // open Page page and link page to this internal node
    // also insert the min key of page into this one
    // no need to increment its size
    public void add(Page<Key, Value> page) {
        Key minKey = page.keys.min();
        keys.put(minKey, new Link(page.id, page, null, page.n));
    }

    // is this an external page
    public boolean isExternal() {
        return isLeaf;
    }

    // get the value associated with the key in this external page
    public Value get(Key key) {
        if (!isLeaf) throw new UnsupportedOperationException("Cannot get value from an internal node");
        Link link = keys.get(key);
        if (link == null) return null;  // key does not exist
        assert link.value != null;
        return link.value;
    }

    // check whether the value associated with the key exists in this external page
    public boolean contains(Key key) {
        if (!isLeaf) throw new UnsupportedOperationException("Cannot check value in an internal node");
        return keys.contains(key);
    }

    // get the next page of this internal page
    // if not in memory, then load the next page from given path
    public Page<Key, Value> next(Key key) throws IOException, ClassNotFoundException {
        if (isLeaf) return null;
        Link link = keys.get(keys.floor(key));
        return link.open();
    }

    public boolean isFull() {
        return keys.size() == M;
    }

    // split this page into two and return the created page with larger keys
    // split pages are both internal or external
    public Page<Key, Value> split(String newId) {
        Page<Key, Value> newPage = new Page<>(newId, isLeaf);
        for (int i = 0; i < M; i += 2) {
            Key key = keys.max();           // get the max key
            Link link = keys.get(key);      // get the link of the max key
            keys.delete(key);               // remove max key from left half
            newPage.keys.put(key, link);
            newPage.n += size(link);         // get the size of the subtree
        }
        n -= newPage.n;
        return newPage;
    }

    public Iterable<Key> keys() {
        return keys.keys();
    }

    public int size() {
        return n;
    }

    private int size(Link link){
        return link.size;
    }

    public void incrementSize(Key key) {
        if (isLeaf) throw new UnsupportedOperationException("Cannot increment link size in an external node");
        Link link = keys.get(keys.floor(key));
        link.size++;
    }

    public void updateSize(){
        n = 0;
        for (Key key : keys.keys())
            n += size(keys.get(key));
    }

    public Key floor(Key key) {
        return keys.floor(key);
    }

    public Key ceiling(Key key) {
        return keys.ceiling(key);
    }

    // rank of the key for external page
    // number of keys to the left of the interval for internal page
    public int rank(Key key) {
        if (isLeaf) return keys.rank(key);
        int r = 0;
        int prev = 0;
        for (Key k : keys.keys(keys.min(), key)) {
            if (k.compareTo(key) <= 0)
                r += prev;
            prev = size(keys.get(k));
        }
        return r;
    }

    // select the key ranked k for external page
    // select the interval where ranked k key lies for internal page
    public Key select(int k) {
        if (isLeaf) return keys.select(k);
        int r = 0;
        Key selected = null;
        for (Key key : keys.keys()) {
            r += size(keys.get(key));
            if (k < r) {
                selected = key;
                break;
            }
        }
        return selected;
    }

    public Key min() {
        return keys.min();
    }

    public Key max() {
        return keys.max();
    }

    public String getId() {
        return id;
    }

    private String getFilename(String id) {
        return path + id + ".bin";
    }
}
