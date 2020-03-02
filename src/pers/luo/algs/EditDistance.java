package pers.luo.algs;

import java.sql.SQLSyntaxErrorException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Calculate edit distance between x and y
 *
 * @author Luo Wenyang
 */
public class EditDistance {
    private static final int COPY = 1;
    private static final int REPLACE = 2;
    private static final int DELETE = 3;
    private static final int INSERT = 4;
    private static final int TWIDDLE = 5;
    private static final int KILL = 6;

    private int dist;
    private String[] ops;

    public EditDistance(String x, String y, int[] cost) {
        int m = x.length();
        int n = y.length();
        int kill = -1;
        int[][] dist = new int[m+1][n+1];
        int[][] ops = new int[m+1][n+1];
        for (int j = 1; j <= n; j++) {
            dist[0][j] = dist[0][j-1] + cost[INSERT];
            ops[0][j] = INSERT;
        }
        for (int i = 1; i <= m; i++) {
            dist[i][0] = dist[i-1][0] + cost[DELETE];
            ops[i][0] = DELETE;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int c = Integer.MAX_VALUE;
                char xi = x.charAt(i - 1);
                char yj = y.charAt(j - 1);
                if (xi == yj && cost[COPY] + dist[i - 1][j - 1] < c) {
                    c = cost[COPY] + dist[i - 1][j - 1];
                    ops[i][j] = COPY;
                }
                if (cost[DELETE] + dist[i - 1][j] < c) {
                    c = cost[DELETE] + dist[i - 1][j];
                    ops[i][j] = DELETE;
                }
                if (cost[INSERT] + dist[i][j - 1] < c) {
                    c = cost[INSERT] + dist[i][j - 1];
                    ops[i][j] = INSERT;
                }
                if (cost[REPLACE] + dist[i - 1][j - 1] < c) {
                    c = cost[REPLACE] + dist[i - 1][j - 1];
                    ops[i][j] = REPLACE;
                }
                if ((i >= 2 && j >= 2) && (xi == y.charAt(j - 2) && yj == x.charAt(i - 2))
                        && cost[TWIDDLE] + dist[i - 2][j - 2] < c) {
                    c = cost[TWIDDLE] + dist[i - 2][j - 2];
                    ops[i][j] = TWIDDLE;
                }
                if (j == n && cost[KILL] + dist[i][j] < dist[m][n]) {
                    dist[m][n] = cost[KILL] + dist[i][j];
                    ops[m][n] = KILL;
                    kill = i;
                }
                dist[i][j] = c;
            }
        }
        this.dist = dist[m][n];
        StringBuilder s = new StringBuilder();
        int i = m, j = n;
        while (i > 0 || j > 0) {
            int op = ops[i][j];
            switch (op) {
                case COPY:
                    s.append("COPY ").append(x.charAt(i-1)).append("\n");
                    i--; j--;
                    break;
                case REPLACE:
                    s.append("REPLACE ").append(x.charAt(i-1)).append(" to ").append(y.charAt(j - 1)).append("\n");
                    i--; j--;
                    break;
                case DELETE:
                    s.append("DELETE ").append(x.charAt(i-1)).append("\n");
                    i--;
                    break;
                case INSERT:
                    s.append("INSERT ").append(y.charAt(j-1)).append("\n");
                    j--;
                    break;
                case TWIDDLE:
                    s.append("TWIDDLE ").append(x.charAt(i-1)).append(x.charAt(i-2)).append(" to ").append(y.charAt(j-1)).append(y.charAt(j-2)).append("\n");
                    i -= 2; j -= 2;
                    break;
                case KILL:
                    s.append("KILL from ").append(i);
                    i = kill;
                    break;
                default:
                    s.append("ERROR");
            }
        }
        String[] t = s.toString().split("\n");
        this.ops = new String[t.length];
        for (int k = 0; k < t.length; k++)
            this.ops[k] = t[t.length-k-1];
    }

    public int dist() {
        return dist;
    }

    public String[] ops() {
        return ops;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] cost = {Integer.MAX_VALUE, -1, 1, 2, 2, Integer.MAX_VALUE, Integer.MAX_VALUE};
        while (scanner.hasNextLine()) {
            String x = scanner.nextLine();
            if (x.equals("exit")) break;
            String y = scanner.next();
            EditDistance e = new EditDistance(x, y, cost);
            System.out.println("edit distance: " + e.dist());
            System.out.println("ops: ");
            for (String s : e.ops())
                System.out.println(s);
        }
    }
}
