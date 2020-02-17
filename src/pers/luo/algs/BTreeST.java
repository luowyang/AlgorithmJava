package pers.luo.algs;

import java.io.*;
import java.util.Scanner;

/**
 * B-tree implementation of an ordered symbol table
 * @param <Key>
 * @param <Value>
 *
 * @author Luo Wenyang
 */
@SuppressWarnings("unchecked")
public class BTreeST<Key extends Comparable<Key>, Value> implements java.io.Serializable {

    private String rootPath;                    // path of the root
    private PagePool pool;

    private transient Page<Key, Value> root;    // root page of the B tree
    private transient PageCache<Key, Value> cache = new PageCache<>();

    public BTreeST(Key sentinel) throws IOException, ClassNotFoundException {
        pool = new PagePool();
        root = new Page<>(pool.fetch(), true);
        put(sentinel, null);
    }

    public BTreeST(FileInputStream fileIn) throws IOException, ClassNotFoundException {
        // load B-tree
        ObjectInputStream in = new ObjectInputStream(fileIn);
        BTreeST<Key, Value> read = (BTreeST<Key, Value>) in.readObject();
        in.close();
        fileIn.close();
        this.rootPath = read.rootPath;
        this.pool = read.pool;
        // load root page
        fileIn = new FileInputStream(rootPath);
        in = new ObjectInputStream(fileIn);
        root = (Page<Key, Value>) in.readObject();
        in.close();
        fileIn.close();
        root.createLink();
    }

    public void put(Key key, Value value) throws IOException, ClassNotFoundException {
        put(root, key, value);
        if (root.isFull()) {
            Page<Key, Value> leftHalf = root;
            Page<Key, Value> rightHalf = root.split(pool.fetch());
            root = new Page<>(pool.fetch(), false);
            root.add(leftHalf);
            root.add(rightHalf);
            // update root's size
            root.updateSize();
        }
    }

    // return whether a new key has been added
    private boolean put(Page<Key, Value> page, Key key, Value value) throws IOException, ClassNotFoundException {
        if (page == null) throw new RuntimeException("Page is null");
        if (page.isExternal()) {
            return page.add(key, value);    // leaf will maintain its size
        }
        Page<Key, Value> next = page.next(key);
        cache.visit(next);
        boolean newKey = put(next, key, value);
        if (newKey)
            page.incrementSize();           // maintain this page's size
        if (next.isFull())
            page.add(next.split(pool.fetch()));
        //next.close();
        return newKey;
    }

    public Value get(Key key) throws IOException, ClassNotFoundException {
        Page<Key, Value> page = root;
        while (!page.isExternal()) {
            page = page.next(key);
            cache.visit(page);
        }
        return page.get(key);
    }

    public boolean contains(Key key) throws IOException, ClassNotFoundException {
        Page<Key, Value> page = root;
        while (!page.isExternal()) {
            page = page.next(key);
            cache.visit(page);
        }
        return page.contains(key);
    }

    public int size() {
        return root.size() - 1;     // minus the sentinel
    }

    public boolean isEmpty() {
        return root.size() == 1;
    }

    public Key min() throws IOException, ClassNotFoundException {
        Page<Key, Value> page = root;
        while (!page.isExternal()) {
            page = page.next(page.min());
            cache.visit(page);
        }
        return page.select(1);  // minus the sentinel
    }

    public Key max() throws IOException, ClassNotFoundException {
        Page<Key, Value> page = root;
        while (!page.isExternal()) {
            page = page.next(page.max());
            cache.visit(page);
        }
        return page.max();  // minus the sentinel
    }

    // key is in [key1, key2) so floor must be in the same interval
    public Key floor(Key key) throws IOException, ClassNotFoundException {
        Page<Key, Value> page = root;
        while (!page.isExternal()) {
            page = page.next(key);
            cache.visit(page);
        }
        return page.floor(key);
    }

    // key is in [key1, key2) but ceiling may be key2
    // if all keys in [key1, key2) are smaller than key
    public Key ceiling(Key key) throws IOException, ClassNotFoundException {
        Page<Key, Value> page = root;
        Key candidate = null;
        while (!page.isExternal()) {
            candidate = page.ceiling(key);
            page = page.next(key);
            cache.visit(page);
        }
        Key ceil = page.ceiling(key);
        return ceil == null ? candidate : ceil;
    }

    public int rank(Key key) throws IOException, ClassNotFoundException {
        Page<Key, Value> page = root;
        int r = 0;
        while (!page.isExternal()) {
            r += page.rank(key);
            page = page.next(key);
            cache.visit(page);
        }
        return r + page.rank(key) - 1;      // minus the sentinel
    }

    public Key select(int k) throws IOException, ClassNotFoundException {
        Page<Key, Value> page = root;
        k++;    // add the sentinel
        while (!page.isExternal()) {
            Key key = page.select(k);
            if (key == null) return null;
            k -= page.rank(key);
            page = page.next(key);
            cache.visit(page);
        }
        return page.select(k);
    }

    public void deleteMin() throws IOException, ClassNotFoundException {
        delete(min());
    }

    public void deleteMax() throws IOException, ClassNotFoundException {
        delete(max());
    }

    public void delete(Key key) throws IOException, ClassNotFoundException {
        if (key == null) throw new IllegalArgumentException("Null key");
        delete(root, key);
    }

    private boolean delete(Page<Key, Value> page, Key key) throws IOException, ClassNotFoundException {
        if (page.isExternal()) return page.delete(key);
        Page<Key, Value> next = page.next(key);
        cache.visit(next);
        Key nextMin = next.min();
        if (!delete(next, key)) return false;
        page.decrementSize();
        if (nextMin.compareTo(key) == 0) {  // check if the separator has been deleted
            nextMin = next.min();
            page.updateKey(key, nextMin);
        }
        Page<Key, Value> sib = page.sibling(nextMin);
        if (sib == null) {              // next has no sibling, indicating that page is root
            pool.release(root.getId());
            root = next;
            return true;
        }
        cache.visit(sib);
        if (next.lessHalf()) {    // needs merging or transferring
            Key sibMin = sib.min();
            if (sib.isHalf()) {             // needs merging
                next.merge(sib);
                page.delete(sibMin);
                page.updateKey(nextMin, next.min());
                cache.remove(sib);
                pool.release(sib.getId());
            }
            else {                          // needs transferring
                sib.transfer(next);
                if (nextMin.compareTo(sibMin) < 0)
                    page.updateKey(sibMin, sib.min());
                else
                    page.updateKey(nextMin, next.min());
            }
        }
        return true;
    }

    public Iterable<Key> keys(Key lo, Key hi) throws IOException, ClassNotFoundException {
        Queue<Key> queue = new Queue<>();
        collect(root, lo, hi, queue);
        queue.dequeue();    // delete the sentinel
        return queue;
    }

    private void collect(Page<Key, Value> page, Key lo, Key hi, Queue<Key> queue) throws IOException, ClassNotFoundException {
        if (page == null) throw new RuntimeException("Page is null");
        cache.visit(page);
        if (page.isExternal()) {
            for (Key key : page.keys())
                if (key.compareTo(lo) >= 0 && key.compareTo(hi) <= 0)
                    queue.enqueue(key);
            return;
        }
        for (Key key : page.keys())
            if (key.compareTo(hi) <= 0)
                collect(page.next(key), lo, hi, queue);
    }

    public Iterable<Key> keys() throws IOException, ClassNotFoundException {
        return keys(min(), max());
    }

    /**
     * IO methods
     */
    public void close(String path) throws IOException, ClassNotFoundException {
        rootPath = String.format("./data/BTree/Page%04d.bin", root.getId());
        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
        fileOut.close();
        close(root);
    }

    private void close(Page<Key, Value> page) throws IOException, ClassNotFoundException {
        if (page.isExternal()) {
            page.close();
            cache.remove(page);
            return;
        }
        for (Key key : page.keys())
            if (page.probe(key)) close(page.next(key));
        page.close();
        cache.remove(page);
    }

    /**
     * print methods
     */
    public void printTree() throws IOException, ClassNotFoundException {
        printTree(root);
        System.out.println("-----------------------------------------------------");
    }

    private void printTree(Page<Key, Value> page) throws IOException, ClassNotFoundException {
        System.out.print((page.isExternal()?"Leaf ":"Node ") + String.format("%s, keys(%d): ", page.getId(), page.size()));
        for (Key key: page.keys())
            System.out.print(key + "  ");
        System.out.println();
        if (page.isExternal()) return;
        for (Key key : page.keys())
            printTree(page.next(key));
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String path = "./data/BTree/btree.bin";
        BTreeST<String, Integer> btree;
        if (args[0].equals("build"))
            btree = buildTree(path, args[1]);
        else
            btree = loadTree(path);
        Scanner scanner = new Scanner(System.in);
        // test size()
        System.out.println("size: " + btree.size());
        // test min()
        System.out.println("min: " + btree.min());
        // test max()
        System.out.println("max: " + btree.max());
        btree.cache.printPages();
        // test floor()
        System.out.println("test floor():");
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            System.out.println("  floor: " + btree.floor(s));
        }
        btree.cache.printPages();
        // test ceiling()
        System.out.println("test ceiling():");
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            System.out.println("  ceiling: " + btree.ceiling(s));
        }
        // test rank()
        System.out.println("test rank():");
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            System.out.println("  rank: " + btree.rank(s));
        }
        // test select()
        System.out.println("test select():");
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            System.out.println("  select: " + btree.select(Integer.parseInt(s)));
        }
        // test delete()
        System.out.println("test delete():");
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            btree.delete(s);
            btree.printTree();
        }
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            if (btree.contains(s))
                System.out.println("  " + btree.get(s));
            else
                System.out.println("  no record");
        }
        btree.close(path);
    }

    private static BTreeST<String, Integer> buildTree(String path, String filename) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(filename));
        BTreeST<String, Integer> btree = new BTreeST<>("");
        Stopwatch timer = new Stopwatch();
        while (scanner.hasNext()) {
            String s = scanner.next();
            if (btree.contains(s)) {
                int k = btree.get(s);
                btree.put(s, k + 1);
            }
            else
                btree.put(s, 1);
        }
        System.out.println("Read finished in " + timer.elapsedTime() + " seconds");
        scanner.close();
        btree.printTree();
        timer = new Stopwatch();
        btree.close(path);
        System.out.println("Save finished in " + timer.elapsedTime() + " seconds");
        return btree;
    }

    private static BTreeST<String, Integer> loadTree(String path) throws IOException, ClassNotFoundException {
        Stopwatch timer = new Stopwatch();
        BTreeST<String, Integer> btree = new BTreeST<>(new FileInputStream(path));
        System.out.println("Load finished in " + timer.elapsedTime() + " seconds");
        return btree;
    }
}
