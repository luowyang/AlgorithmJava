package pers.luo.algs;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * Run length coding compression algorithm
 *
 * Description: the first bit is always asserted to be 0,
 * if not, a 0 run length code will be inserted int the beginning.
 * Run length is encoded by 8 bits, and any length longer than 255 will be broke down
 * by inserting a 0 after the code of the first 255 bits.
 *
 * @author Luo Wenyang
 */
public class RunLength {
    public static void compress() {
        char cnt = 0;               // run length count
        boolean b, old = false;     // false for 0, true for 1
        while (!BinaryStdIn.isEmpty()) {
            b = BinaryStdIn.readBoolean();
            if (b != old) {         // end of a run length
                BinaryStdOut.write(cnt);
                cnt = 0;            // clear counter
                old = !old;         // set old to current bit
            }
            else if (cnt == 255) {  // counter overflow
                BinaryStdOut.write(cnt);
                cnt = 0;            // reset counter
                BinaryStdOut.write(cnt);
            }
            cnt++;
        }
        BinaryStdOut.write(cnt);    // don't forget to write the last count
        BinaryStdOut.close();       // clear buffer
    }

    public static void expand() {
        boolean b = false;          // initially 0
        while (!BinaryStdIn.isEmpty()) {
            char cnt = BinaryStdIn.readChar();  // read in run length
            for (int i = 0; i < cnt; i++)
                BinaryStdOut.write(b);
            b = !b;
        }
    }
}
