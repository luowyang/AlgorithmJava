package pers.luo.algs;

/**
 * Abstract data structure for representing alphabet
 * @author Luo Wenyang
 **/
public class Alphabet {
    // default charset
    public static final Alphabet BINARY      = new Alphabet("01");
    public static final Alphabet DNA         = new Alphabet("ACTG");
    public static final Alphabet OCTAL       = new Alphabet("01234567");
    public static final Alphabet DECIMAL     = new Alphabet("0123456789");
    public static final Alphabet HEXADECIMAL = new Alphabet("0123456789ABCDEF");
    public static final Alphabet PROTEIN     = new Alphabet("ACDEFGHIJKLMNPQRSTVWY");
    public static final Alphabet LOWERCASE   = new Alphabet("abcdefghijklmnopqrstuvwxyz");
    public static final Alphabet UPPERCASE   = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    public static final Alphabet BASE64      = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");

    // private data member


    // create alphabet with string s
    public Alphabet(String s) {

    }

    // get the char in alphabet with the index
    public char toChar(int index) {
        return 0;
    }

    // get the index between 0 and R-1 of char c
    public int toIndex(char c) {
        return 0;
    }

    // check whether tha alphabet contains char c
    public boolean contains(char c) {
        return false;
    }

    // get the number of characters in the alphabet
    public int R() {
        return 0;
    }

    // get the digits needed to store an index of the alphabet
    public int logR() {
        return 0;
    }

    // convert a string to an array of indices
    public int[] toIndices(String s) {
        return null;
    }

    // convert indices to string
    public String toChars(int[] indices) {
        return null;
    }
}
