package pers.luo.algs;

import java.io.*;

/**
 * Page of M-order B-tree set, M is adjustable.
 * Implemented with a binary search tree
 * @param <Key>
 *
 * @author Luo Wenyang
 */
public class PageSet<Key extends Comparable<Key>> implements java.io.Serializable {
    private static final int M = 500;  // node capacity
    private boolean isLeaf;             // true if external, false if internal
    private AVLBST<Key, Link> keys;     // binary search tree to store keys
    private String id;                  // id of this page
    private static String path = "./data/BTree/Page";

    private class Link implements java.io.Serializable {
        String id;
        transient PageSet<Key> next;
        public Link(String id, PageSet<Key> next) {
            this.id = id;
            this.next = next;
        }
    }

    // create and open a page
    public PageSet(String id, boolean bottom) {
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

    // add a key to this external page
    public void add(Key key) {
        if (!isLeaf) throw new UnsupportedOperationException("Cannot add keys to an internal node");
        keys.put(key, new Link(null, null));
    }

    // open Page page and link page to this internal node
    // also insert the min key of page into this one
    public void add(PageSet<Key> page) {
        Key minKey = page.keys.min();
        keys.put(minKey, new Link(page.id, page));
    }

    // is this an external page
    public boolean isExternal() {
        return isLeaf;
    }

    public boolean contains(Key key) {
        if (isLeaf) return keys.contains(key);
        PageSet<Key> next = keys.get(keys.floor(key)).next;
        return next.contains(key);
    }

    public PageSet<Key> next(Key key) throws IOException, ClassNotFoundException {
        if (isLeaf) throw new UnsupportedOperationException("Cannot use next() on an external node");
        Link link = keys.get(keys.floor(key));
        PageSet<Key> nextPage = link.next;
        if (nextPage == null) {
            FileInputStream fileIn = new FileInputStream(getFilename(link.id));
            ObjectInputStream in = new ObjectInputStream(fileIn);
            nextPage = (PageSet<Key>) in.readObject();
            link.next = nextPage;
            in.close();
            fileIn.close();
            System.out.println("Page " + nextPage.id + " has been loaded");
        }
        return nextPage;
    }

    public boolean isFull() {
        return keys.size() == M;
    }

    // split this page into two and return the created page with larger keys
    // split pages are both internal or external
    public PageSet<Key> split(String newId) {
        PageSet<Key> newPage = new PageSet<>(newId, isLeaf);
        for (int i = 0; i < M; i += 2) {
            Key key = keys.max();
            Link link = keys.get(key);
            keys.delete(key);
            newPage.keys.put(key, link);
        }
        return newPage;
    }

    public Iterable<Key> keys() {
        return keys.keys();
    }

    public String getId() {
        return id;
    }

    private String getFilename(String id) {
        return path + id + ".bin";
    }
}
