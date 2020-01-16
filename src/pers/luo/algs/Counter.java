package pers.luo.algs;

public class Counter {
    private final String name;  // final关键字说明初始化后就不能修改
    private int count;

    public Counter(String id)   // 这是构造函数，不需要指出返回值类型
    { name = id; }

    public void increment()
    { count++; }

    public int tally()
    { return count; }

    public String toString()
    { return count + " " + name; }

    public boolean equals(Object that)
    {
        if (this == that) return true;
        if (that == null) return false;
        if (this.getClass() != that.getClass()) return false;
        Counter c = (Counter) that;
        if (this.count != c.count) return false;
        // if (!this.name.equals(c.name)) return false;
        return true;
    }

    // Test use case
    public static void main(String[] args)
    {
        Counter heads = new Counter("heads");
        Counter tails = new Counter("tails");

        heads.increment();
        heads.increment();
        tails.increment();

        System.out.println(heads + " " + tails);
        System.out.println(heads.tally() + tails.tally());
    }
}
