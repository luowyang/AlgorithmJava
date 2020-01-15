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

    public static void main(String[] args)
    {
        int m = Integer.parseInt(args[0]);
        int d = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        Date date = new Date(m, d, y);
        System.out.println(date);
    }
}
