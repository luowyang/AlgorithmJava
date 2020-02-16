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
    private transient Page<Key, Value> root;
    private String rootPath;
    private int cnt = 1;

    public BTreeST(Key sentinel) throws IOException, ClassNotFoundException {
        root = new Page<>("0000", true);
        put(sentinel, null);
    }

    public BTreeST(FileInputStream fileIn) throws IOException, ClassNotFoundException {
        // load B-tree
        ObjectInputStream in = new ObjectInputStream(fileIn);
        BTreeST<Key, Value> read = (BTreeST<Key, Value>) in.readObject();
        in.close();
        fileIn.close();
        this.rootPath = read.rootPath;
        this.cnt = read.cnt;
        // load root page
        fileIn = new FileInputStream(rootPath);
        in = new ObjectInputStream(fileIn);
        root = (Page<Key, Value>) in.readObject();
        in.close();
        fileIn.close();
        root.updateSize();
    }

    public void put(Key key, Value value) throws IOException, ClassNotFoundException {
        put(root, key, value);
        if (root.isFull()) {
            Page<Key, Value> leftHalf = root;
            Page<Key, Value> rightHalf = root.split(distributeId());
            root = new Page<>(distributeId(), false);
            root.add(leftHalf);
            root.add(rightHalf);
            root.updateSize();
        }
    }

    // return whether a new key has been added
    private boolean put(Page<Key, Value> page, Key key, Value value) throws IOException, ClassNotFoundException {
        if (page.isExternal()) {
            return page.add(key, value);
        }
        Page<Key, Value> next = page.next(key);
        boolean newKey = put(next, key, value);
        if (newKey)
            page.incrementSize(key);       // increment next page's size
        if (next.isFull())
            page.add(next.split(distributeId()));
        //next.close();
        return newKey;
    }

    public Value get(Key key) throws IOException, ClassNotFoundException {
        Page<Key, Value> page = root;
        while (!page.isExternal())
            page = page.next(key);
        return page.get(key);
    }

    public boolean contains(Key key) throws IOException, ClassNotFoundException {
        Page<Key, Value> page = root;
        while (!page.isExternal())
            page = page.next(key);
        return page.contains(key);
    }

    public int size() {
        return root.size() - 1;     // minus the sentinel
    }

    public Key min() throws IOException, ClassNotFoundException {
        Page<Key, Value> page = root;
        while (!page.isExternal())
            page = page.next(page.min());
        return page.select(1);  // minus the sentinel
    }

    public Key max() throws IOException, ClassNotFoundException {
        Page<Key, Value> page = root;
        while (!page.isExternal())
            page = page.next(page.max());
        return page.max();  // minus the sentinel
    }

    // key is in [key1, key2) so floor must be in the same interval
    public Key floor(Key key) throws IOException, ClassNotFoundException {
        Page<Key, Value> page = root;
        while (!page.isExternal())
            page = page.next(key);
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
        }
        return page.select(k);
    }

    public void deleteMin() {

    }

    public void deleteMax() {

    }

    public void delete(Key key) {

    }

    public Iterable<Key> keys(Key lo, Key hi) throws IOException, ClassNotFoundException {
        Queue<Key> queue = new Queue<>();
        collect(root, lo, hi, queue);
        return queue;
    }

    private void collect(Page<Key, Value> page, Key lo, Key hi, Queue<Key> queue) throws IOException, ClassNotFoundException {
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

    private String distributeId() {
        return String.format("%04d", cnt++);
    }

    /**
     * IO methods
     */
    public void close(String path) throws IOException, ClassNotFoundException {
        rootPath = "./data/BTree/Page" + root.getId() + ".bin";
        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
        fileOut.close();
        close(root);
    }

    private void close(Page<Key, Value> page) throws IOException, ClassNotFoundException {
        page.close();
        if (page.isExternal()) return;
        for (Key key : page.keys())
            close(page.next(key));
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
        // test floor()
        System.out.println("test floor():");
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            System.out.println("  floor: " + btree.floor(s));
        }
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
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            if (btree.contains(s))
                System.out.println("  " + btree.get(s));
            else
                System.out.println("  no record");
        }
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
