package pers.luo.algs;

public class Date {
    private final int month;
    private final int day;
    private final int year;

    public Date(int month, int day, int year)
    { this.month = month; this.day = day; this.year = year; }

    public int day()
    { return day; }

    public int month()
    { return month; }

    public int year()
    { return year; }

    public String toString()
    { return month() + "/" + day() + "/" + year(); }

    public boolean equals(Object that)
    {
        if (this == that) return true;              // reference test
        if (that == null) return false;             // null test
        if (this.getClass() != that.getClass()) return false;   // Class test
        Date d = (Date) that;                       // type conversion
        if (this.month != d.month) return false;
        if (this.day != d.day) return false;
        if (this.year != d.year) return false;
        return true;
    }

    public static void main(String[] args)
    {
        int m = Integer.parseInt(args[0]);
        int d = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        Date date = new Date(m, d, y);
        System.out.println(date);
    }
}
