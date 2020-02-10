package pers.luo.algs;

import java.util.Scanner;

public class MathSet<Key> {
    private HashSet<Key> set;
    private Key[] universe;

    public MathSet(Key[] universe) {
        if (universe == null) throw new IllegalArgumentException("Universal set must not be null");
        set = new HashSet<>();
        this.universe = universe;
    }

    public void add(Key key) {
        if (key == null) throw new IllegalArgumentException("Key must not be null");
        set.add(key);
    }

    public MathSet<Key> complement() {
        MathSet<Key> com = new MathSet<>(universe);
        for (Key key : universe) if (!set.contains(key)) com.add(key);
        return com;
    }

    public void union(MathSet<Key> that) {
        if (that == null) throw new IllegalArgumentException("Intersection set must not be null");
        for (Key key : that.set.keys())
            set.add(key);
    }

    public void intersect(MathSet<Key> that) {
        if (that == null) throw new IllegalArgumentException("Intersection set must not be null");
        for (Key key : set.keys())
            if (!that.set.contains(key)) set.delete(key);
    }

    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("Key must not be null");
        set.delete(key);
    }

    public boolean contains(Key key) {
        return set.contains(key);
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public Iterable<Key> keys() {
        return set.keys();
    }

    public static void main(String[] args)
    {
        String[] universe = {
                "A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R",
                "S", "T", "U", "V", "W", "X", "Y", "Z"
        };
        MathSet<String> set1 = new MathSet<>(universe);
        MathSet<String> set2 = new MathSet<>(universe);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.next();
            set1.add(s);
        }
        set2.add("B");
        set2.add("T");
        set2.add("E");
        set2.add("A");
        set2.add("R");
        System.out.println("set 1:");
        for (String s : set1.keys())
            System.out.print(s + " ");
        System.out.println();
        System.out.println("set 2:");
        for (String s : set2.keys())
            System.out.print(s + " ");
        System.out.println();
        System.out.println("Complement test:");
        for (String s : set1.complement().keys())
            System.out.print(s + " ");
        System.out.println();
        System.out.println("Intersect test:");
        set1.intersect(set2);
        for (String s : set1.keys())
            System.out.print(s + " ");
        System.out.println();
        System.out.println("Union test:");
        set1.union(set2);
        for (String s : set1.keys())
            System.out.print(s + " ");
        System.out.println();
    }
}
