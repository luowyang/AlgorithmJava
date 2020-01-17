package pers.luo.algs;

import pers.luo.algs.Date;

public class Transaction {
    private String who;
    private Date when;
    private double amount;

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
