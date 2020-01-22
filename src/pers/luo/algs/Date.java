package pers.luo.algs;

public class Date implements Comparable<Date> {
    private final int month;
    private final int day;
    private final int year;

    public Date(int month, int day, int year)
    {
        this.month = month;
        this.day = day;
        this.year = year;
        validate();
    }

    public Date(String date)
    {
        String[] fields = date.split("/");
        this.month = Integer.parseInt(fields[0]);
        this.day = Integer.parseInt(fields[1]);
        this.year = Integer.parseInt(fields[2]);
        validate();
    }

    public int day()
    { return day; }

    public int month()
    { return month; }

    public int year()
    { return year; }

    public String dayOfTheWeek()
    {
        String[] weekday = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        int interval = year - 1;
        int numOfLY = interval/4 - interval/100 + interval/400;
        int days = interval*365 + numOfLY;
        days += daysThisYear();
        return weekday[days % 7];
    }

    @Override
    public String toString()
    { return month() + "/" + day() + "/" + year(); }

    @Override
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

    @Override
    public int compareTo(Date that) {
        if (this.year  > that.year ) return +1;
        if (this.year  < that.year ) return -1;
        if (this.month > that.month) return +1;
        if (this.month < that.month) return -1;
        if (this.day   > that.day  ) return +1;
        if (this.day   < that.day  ) return -1;
        return 0;
    }

    private boolean isLeapYear()
    { return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)); }

    private int daysThisYear()
    {
        int[] daysNY = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int days = 0;
        int N = month - 1;
        for (int i = 1; i <= N; i++)
            days += daysNY[i];
        days += day;
        if (isLeapYear() && month > 2) days++;
        return days;
    }

    private void validate()
    {
        int[] daysNY = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int[] daysLY = {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (month <= 0 || month > 12) throw new RuntimeException("Date month out of boundary");
        if (isLeapYear()) {
            if (day <= 0 || day > daysLY[month]) throw new RuntimeException("Date day out of boundary");
        }
        else {
            if (day <= 0 || day > daysNY[month]) throw new RuntimeException("Date day out of boundary");
        }
    }

    public static void main(String[] args)
    {
        int m = Integer.parseInt(args[0]);
        int d = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        Date date = new Date(m, d, y);
        System.out.println(date);
        System.out.println(date.daysThisYear());
        System.out.println(date.dayOfTheWeek());

        Date date2 = new Date("1/1/1999");
        System.out.println(date2);
    }
}
