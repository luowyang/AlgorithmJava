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

    public static void main(String[] args)  // 测试用例
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
