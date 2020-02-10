package pers.luo.algs;

import pers.luo.algs.Date;

import java.util.Comparator;

public class Transaction implements Comparable<Transaction> {
    private final String who;
    private final Date when;
    private final double amount;

    private int hash = -1;

    public Transaction(String who, Date when, double amount)
    { this.who = who; this.when = when; this.amount = amount; }

    public Transaction(String transaction)
    {
        String[] fields = transaction.split("\\s+");
        who = fields[0];
        when = new Date(fields[1]);
        amount = Double.parseDouble(fields[2]);
    }

    public String who()
    { return who; }

    public Date when()
    { return when; }

    public double amount()
    { return amount; }

    public String toString()
    { return who + " " + when + " " + amount; }

    public boolean equals(Object that)
    {
        if (this == that) return true;
        if (that == null) return false;
        if (this.getClass() != that.getClass()) return false;
        Transaction t = (Transaction) that;
        if (!this.who.equals(t.who)) return false;
        if (!this.when.equals(t.when)) return false;
        if (this.amount != t.amount) return false;
        return true;
    }

    @Override
    public int hashCode() {
        if (hash >= 0) return hash;
        hash = 17;
        hash = 31 * hash + who.hashCode();
        hash = 31 * hash + when.hashCode();
        hash = 31 * hash + ((Double) amount).hashCode();
        return hash;
    }

    public int compareTo(Transaction that)
    {
        if (this.amount > that.amount) return +1;
        if (this.amount < that.amount) return -1;
        return 0;
    }

    public static class WhoOrder implements Comparator<Transaction> {
        public int compare(Transaction v, Transaction w)
        { return v.who.compareTo(w.who); }
    }

    public static class WhenOrder implements Comparator<Transaction> {
        public int compare(Transaction v, Transaction w)
        { return v.when.compareTo(w.when); }
    }

    public static class HowMuchOrder implements Comparator<Transaction> {
        public int compare(Transaction v, Transaction w)
        {
            if (v.amount > w.amount) return +1;
            if (v.amount < w.amount) return -1;
            return 0;
        }
    }

    public static void main(String[] args)
    {
        String who = args[0];
        //Date when = new Date(1,17,2020);
        Date when = new Date(args[1]);
        double amount = Double.parseDouble(args[2]);
        Transaction t1 = new Transaction(who, when, amount);
        System.out.println(t1);
        Transaction t2 = new Transaction("Turing 2/22/2012 196.50");
        System.out.println(t2);
    }
}
