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

    private static class Link<Key extends Comparable<Key>, Value> implements java.io.Serializable {
        transient static String path = "./data/BTree/Page";
        int id;
        transient Page<Key, Value> next;
        Value value;
        int size;                   // number of keys in the subtree pointed by the link
        public Link(int id, Page<Key, Value> next, Value value, int size) {
            this.id = id;           // id of the next page, null if the link contains a value
            this.next = next;       // pointer to the next page, null if no next or unloaded
            this.value = value;     // value is null for internal links
            this.size = size;       // set size
            // update page's ref if page is not null
            if (next != null) next.ref = this;
        }
        // check if the page pointed by the link has been loaded
        // if the link contains a value, it will always return true
        public boolean isOpened() {
            return id <= 0 || next != null;
        }
        /**
         * Open the next page pointed by this link
         * if not already opened, this method will load the page from disk
         * @return opened page
         * @throws IOException IO
         * @throws ClassNotFoundException IO
         */
        public Page<Key, Value> open() throws IOException, ClassNotFoundException {
            if (isOpened()) return next;
            FileInputStream fileIn = new FileInputStream(String.format("%s%04d.bin", path, id));
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Page<Key, Value> page = (Page<Key, Value>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("(DEBUG: Page " + page.id + " has been loaded)");
            next = page;
            page.ref = this;        // update link ref
            return page;
        }
    }

    private transient static final int M = 500;   // node capacity
    private transient static final String path = "./data/BTree/Page";

    private boolean isLeaf;             // true if external, false if internal
    private AVLBST<Key, Link<Key, Value>> keys;     // binary search tree to store keys
    private int id;                  // id of this page

    private transient Link<Key, Value> ref;         // reference to parent's link to this

    /**
     * Create and open a page, also create an associated link
     * @param id        id of the new page
     * @param external  whether create the new page as an external page
     */
    public Page(int id, boolean external) {
        this.id = id;
        isLeaf = external;
        keys = new AVLBST<>();
        // create associated link
        ref = new Link<>(id, this, null, 0);
    }

    /**
     * Close and write back a page and all its descendants
     * @throws IOException IO exceptions
     */
    public void close() throws IOException {
        FileOutputStream fileOut = new FileOutputStream(getFilename(id));
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
        fileOut.close();
        ref.next = null;
    }

    // add a key to this external page, this method will increment this page's size
    // return whether a new key has been added
    public boolean add(Key key, Value value) {
        if (!isLeaf) throw new UnsupportedOperationException("Cannot add keys to an internal node");
        if (!keys.contains(key)) {
            keys.put(key, new Link<>(0, null, value, 1));
            ref.size++;
            return true;
        }
        keys.get(key).value = value;
        return false;
    }

    /**
     * Open page and link page to this internal node
     * also insert the min key of page into this one
     * this page's size will NOT be incremented
     * @param page  page to be added
     */
    public void add(Page<Key, Value> page) {
        Key minKey = page.keys.min();
        keys.put(minKey, page.ref);
    }

    /**
     * Is this an external page?
     * @return  true: external  false: internal
     */
    public boolean isExternal() {
        return isLeaf;
    }

    // get the value associated with the key in this external page
    public Value get(Key key) {
        if (!isLeaf) throw new UnsupportedOperationException("Cannot get value from an internal node");
        Link<Key, Value> link = keys.get(key);
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
        Link<Key, Value> link = keys.get(keys.floor(key));
        return link.open();
    }

    public Page<Key, Value> sibling(Key key) throws IOException, ClassNotFoundException {
        if (isLeaf) return null;
        int r = keys.rank(key);
        Link<Key, Value> link;
        if      (r < keys.size() - 1) link = keys.get(keys.select(r + 1));
        else if (r > 0)               link = keys.get(keys.select(r - 1));
        else                          return null;
        return link.open();
    }

    public boolean isFull() {
        return keys.size() >= M;
    }

    public boolean isHalf() {
        return keys.size() == M/2;
    }

    public boolean lessHalf() {
        return keys.size() < M/2;
    }

    /**
     * Split this page into two and return the created page with larger keys
     * split pages are both internal or external
     * @param newId id of the split new page
     * @return the split new page
     */
    public Page<Key, Value> split(int newId) {
        Page<Key, Value> newPage = new Page<>(newId, isLeaf);
        for (int i = 0; i < M; i += 2) {
            Key key = keys.max();           // get the max key
            Link<Key, Value> link = keys.get(key);      // get the link of the max key
            keys.delete(key);               // remove max key from left half
            newPage.keys.put(key, link);
            newPage.ref.size += size(link); // update size
        }
        // update this page's size
        ref.size -= newPage.ref.size;
        return newPage;
    }

    /**
     * Merge another page to this page
     * @param page page to be merged with this page
     * @return the merged page
     */
    public void merge(Page<Key, Value> page) {
        if (page == null) throw new RuntimeException("Null page");
        for (Key key : page.keys.keys()) {
            Link<Key, Value> link = page.keys.get(key);
            keys.put(key, link);
            ref.size += size(link); // update size
        }
    }

    /**
     * transfer a key from this page to another page
     * the transferred key will be the nearest one to the other page
     * @param page page to be transferred to
     */
    public void transfer(Page<Key, Value> page) {
        if (page == null) throw new RuntimeException("Null page");
        Key key;
        Link<Key, Value> link;
        if (keys.min().compareTo(page.keys.min()) < 0)  // transfer the largest
            key = keys.max();
        else
            key = keys.min();
        link = keys.get(key);
        keys.delete(key);
        ref.size--;         // update size
        page.keys.put(key, link);
        page.ref.size++;    // update size
    }

    public void updateKey(Key oldKey, Key newKey) {
        if (oldKey == null) throw new IllegalArgumentException("Old key is null");
        if (newKey == null) throw new IllegalArgumentException("New key is null");
        if (oldKey.compareTo(newKey) == 0) return;
        Link<Key, Value> link = keys.get(oldKey);
        if (link == null) return;
        keys.delete(oldKey);
        keys.put(newKey, link);
    }

    public Iterable<Key> keys() {
        return keys.keys();
    }

    public void deleteMin() {

    }

    public void deleteMax() {

    }

    /**
     * delete a key from the external page and return null, which will also decrement its size
     * delete a key from the internal page and return the page associated with the deleted key
     * @param key key to be deleted
     * @return null if external or non-existing, otherwise the deleted page
     */
    public boolean delete(Key key) {
        if (isLeaf) {
            if (!keys.contains(key)) return false;
            keys.delete(key);
            ref.size--;
            return true;
        }
        keys.delete(key);
        return false;
    }

    /**
     * size operations
     */

    public int size() {
        if (ref == null) throw new UnsupportedOperationException("Link is null");
        return ref.size;
    }

    private int size(Link<Key, Value> link){
        if (link == null) throw new IllegalArgumentException("Link is null");
        return link.size;
    }

    public void incrementSize() {
        ref.size++;
    }

    public void decrementSize() {
        ref.size--;
    }

    public void updateSize(){
        if (ref == null) throw new UnsupportedOperationException("Link to current page not found");
        ref.size = 0;
        for (Key key : keys.keys())
            ref.size += size(keys.get(key));
    }

    public void createLink() {
        new Link<>(id, this, null, 0);
        this.updateSize();
    }

    /**
     * ordered operations
     */

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

    /**
     * helpers
     */

    public boolean probe(Key key) {
        return keys.get(key).isOpened();
    }

    public int getId() {
        return id;
    }

    private String getFilename(int id) {
        return String.format("%s%04d.bin", path, id);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (this.getClass() != that.getClass()) return false;
        Page<Key, Value> t = (Page<Key, Value>) that;
        return this.id == t.id;
    }

    public static void main(String[] args) {
        Page<String, Integer> page = new Page<>(1, true);
        page.createLink();
        page.add("ha", 1);
        System.out.println(page.isExternal());
        page.incrementSize();
        System.out.println(page.ceiling("h"));
    }
}
