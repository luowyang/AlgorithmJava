package pers.luo.algs;

import java.util.Arrays;

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
    private char[] alphabet;    // charset
    private int[] indices;      // indices['c'] is the index of char 'c'
    private int R;              // radix

    // create alphabet with string s
    public Alphabet(String s) {
        R = s.length();
        alphabet = s.toCharArray();
        indices = new int[Character.MAX_VALUE];
        // -1 means not in charset
        Arrays.fill(indices, -1);   // -1 means not in charset
        int index = 0;
        for (char c : alphabet)
            indices[c] = index++;
    }

    // get the char in alphabet with the index
    public char toChar(int index) {
        return alphabet[index];
    }

    // get the index between 0 and R-1 of char c
    public int toIndex(char c) {
        return indices[c];
    }

    // check whether tha alphabet contains char c
    public boolean contains(char c) {
        return indices[c] >= 0;
    }

    // get the number of characters in the alphabet
    public int R() {
        return R;
    }

    // get the digits needed to store an index of the alphabet
    public int logR() {
        return (int)(Math.log(R) / Math.log(2));
    }

    // convert a string to an array of indices
    public int[] toIndices(String s) {
        int[] result = new int[s.length()];
        for (int i = 0; i < result.length; i++)
            result[i] = indices[s.charAt(i)];
        return result;
    }

    // convert indices to string
    public String toChars(int[] indices) {
        StringBuilder s = new StringBuilder();
        for (int index : indices)
            s.append(alphabet[index]);
        return s.toString();
    }

    public static void main(String[] args) {
        Alphabet uppercase = Alphabet.UPPERCASE;
        int[] indices = uppercase.toIndices("THISISATEST");
        for (int i : indices)
            System.out.print(i + " ");
        System.out.println();
        int[] ints = {19, 4, 18, 19};
        String s = uppercase.toChars(ints);
        System.out.println(s);
    }
}
