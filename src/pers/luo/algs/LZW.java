package pers.luo.algs;

import edu.princeton.cs.algs4.BinaryOut;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.TopologicalX;

/**
 * Lempel-Ziv-Welch compression algorithm
 * @author Luo Wenyang
 */
public class LZW {
    private static final int R = 256;       // alphabet size
    private static final int L = 4096;      // # of codewords
    private static final int W = 12;        // codeword width

    public static void compress(String bits) {
        BinaryOut out = new BinaryOut(bits);
        String input = BinaryStdIn.readString();    // input string
        // initialize codeword table
        TST<Integer> tst = new TST<>();
        for (char c = 0; c < R; c++)
            tst.put(Character.toString(c), (int) c);
        int code = R + 1;   // codeword to distribute, R is EOF
        while (input.length() > 0) {
            // find longest prefix and write its code
            String s = tst.longestPrefixOf(input);
            out.write(tst.get(s), W);
            // lookahead
            int t = s.length();
            if (t < input.length() && code < L)
                tst.put(input.substring(0, t+1), code++);
            input = input.substring(t);             // read s from input
        }
        out.write(R, W);    // write EOF
        out.close();
    }

    public static void expand(String bytes) {
        BinaryOut out = new BinaryOut(bytes);
        String[] st = new String[L];
        int code;   // codeword to distribute, R is EOF
        // initialize decoding table
        for (code = 0; code < R; code++)
            st[code] = Character.toString((char) code);
        st[code++] = "";    // EOF is "", also move code to R+1
        int codeword = BinaryStdIn.readInt(W);  // read the first codeword
        String val = st[codeword];              // get corresponding string
        while (true) {
            out.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;           // EOF
            String s = st[codeword];
            if (code == codeword)               // read a codeword to be completed
                s = val + val.charAt(0);        // val and val's first char is s
            if (code < L)
                st[code++] = val + s.charAt(0); // update decoding table
            val = s;
        }
        out.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-"))
            compress(args[1]);
        else
            expand(args[1]);
    }
}
