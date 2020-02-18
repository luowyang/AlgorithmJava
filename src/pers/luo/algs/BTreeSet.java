package pers.luo.algs;

import javafx.scene.paint.Stop;

import java.io.*;
import java.util.Scanner;

/**
 * M-order B-tree set, M is adjustable.
 * Implemented with a binary search tree
 * @param <Key>
 *
 * @author Luo Wenyang
 */
@SuppressWarnings("unchecked")
public class BTreeSet<Key extends Comparable<Key>> implements java.io.Serializable {
    private transient PageSet<Key> root;
    private String rootPath;
    private int cnt = 1;

    public BTreeSet(Key sentinel) throws IOException, ClassNotFoundException {
        root = new PageSet<>("0000", true);
        add(sentinel);
    }

    public BTreeSet(FileInputStream fileIn) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(fileIn);
        BTreeSet<Key> read = (BTreeSet<Key>) in.readObject();
        in.close();
        fileIn.close();
        this.rootPath = read.rootPath;
        this.cnt = read.cnt;
        fileIn = new FileInputStream(rootPath);
        in = new ObjectInputStream(fileIn);
        root = (PageSet<Key>) in.readObject();
        in.close();
        fileIn.close();
    }

    public boolean contains(Key key) throws IOException, ClassNotFoundException {
        PageSet<Key> page = root;
        while (!page.isExternal())
            page = page.next(key);
        return page.contains(key);
    }

    public void add(Key key) throws IOException, ClassNotFoundException {
        add(root, key);
        if (root.isFull()) {
            PageSet<Key> leftHalf = root;
            PageSet<Key> rightHalf = root.split(distributeId());
            root = new PageSet<>(distributeId(), false);
            root.add(leftHalf);
            root.add(rightHalf);
        }
    }

    private void add(PageSet<Key> page, Key key) throws IOException, ClassNotFoundException {
        if (page.isExternal()) {
            page.add(key);
            return;
        }
        PageSet<Key> next = page.next(key);
        add(next, key);
        if (next.isFull())
            page.add(next.split(distributeId()));
        //next.close();
    }

    private String distributeId() {
        return String.format("%04d", cnt++);
    }

    public void printTree() throws IOException, ClassNotFoundException {
        printTree(root);
    }

    private void printTree(PageSet<Key> page) throws IOException, ClassNotFoundException {
        System.out.print((page.isExternal()?"Leaf":"Node") + " id: " + page.getId() + ", keys: ");
        for (Key key: page.keys())
            System.out.print(key + "  ");
        System.out.println();
        if (page.isExternal()) return;
        for (Key key : page.keys())
            printTree(page.next(key));
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

    private void close(PageSet<Key> page) throws IOException, ClassNotFoundException {
        page.close();
        if (page.isExternal()) return;
        for (Key key : page.keys())
            close(page.next(key));
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String path = "./data/BTree/btree.bin";
        BTreeSet<String> btree;
        if (args[0].equals("build"))
            btree = buildTree(path, args[1]);
        else
            btree = loadTree(path);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String query = scanner.nextLine();
            System.out.println("In BTree: " + btree.contains(query));
        }
    }

    private static BTreeSet<String> buildTree(String path, String filename) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(filename));
        BTreeSet<String> btree = new BTreeSet<>("");
        Stopwatch timer = new Stopwatch();
        while (scanner.hasNext()) {
            String s = scanner.next();
            btree.add(s);
        }
        System.out.println("Read finished in " + timer.elapsedTime() + " seconds");
        scanner.close();
        timer = new Stopwatch();
        btree.close(path);
        System.out.println("Save finished in " + timer.elapsedTime() + " seconds");
        return btree;
    }

    private static BTreeSet<String> loadTree(String path) throws IOException, ClassNotFoundException {
        Stopwatch timer = new Stopwatch();
        BTreeSet<String> btree = new BTreeSet<>(new FileInputStream(path));
        System.out.println("Load finished in " + timer.elapsedTime() + " seconds");
        return btree;
    }
}
