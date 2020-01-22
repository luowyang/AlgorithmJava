package pers.luo.algs;

/*
 * This lib evaluate expression using Shunting Yard algorithm
 */
public class Evaluate {

    private static boolean isOperand(String s)
    {
        if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/"))
            return true;
        else
            return false;
    }

    public static String infixToPostfix(String infix)
    {
        String[] tmp = infix.split("\\s+");
        Stack<String> output = new Stack<>();
        Stack<String> ops = new Stack<>();
        for (String s : tmp) {
            if (s.equals("(")) {
                ops.push(s);
            }
            else if (s.equals(")")) {
                for (String ss = ops.pop(); !ss.equals("("); ss = ops.pop())
                    output.push(ss);
            }
            else if (isOperand(s)) {
                ops.push(s);
            }
            else {
                output.push(s);
            }
        }
        while (!ops.isEmpty()) output.push(ops.pop());
        String postfix = output.pop();
        while (!output.isEmpty()) postfix = output.pop() + " " + postfix;
        return postfix;
    }

    public static double postfix(String pf)
    {
        String[] tmp = pf.split("\\s+");
        Stack<Double> output = new Stack<>();
        Stack<String> ops = new Stack<>();
        for (String s : tmp) {
            if (s.equals("+")) {
                output.push(output.pop() + output.pop());
            }
            else if (s.equals("-")) {
                output.push(output.pop() - output.pop());
            }
            else if (s.equals("*")) {
                output.push(output.pop() * output.pop());
            }
            else if (s.equals("/")) {
                output.push(output.pop() / output.pop());
            }
            else {
                output.push(Double.parseDouble(s));
            }
        }
        return output.pop();
    }

    public static void main(String[] args)
    {
        String infix = "( ( 1 + 2 ) * ( ( 3 - 4 ) * ( 5 - 6 ) ) )";
        String pf = infixToPostfix(infix);

        System.out.println("infix: " + infix);
        System.out.println("infix to postfix: " + pf);
        System.out.println("evaluate postfix: " + postfix(pf));
    }
}
