package com.alibaba.json.test;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.text.NumberFormat;
import java.util.BitSet;
import java.util.Random;

/**
 * Created by wenshao on 08/01/2017.
 */
public class FNV32_CollisionTest_All extends TestCase {

    char[] digLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_".toCharArray();
    //char[] digLetters = "0123456789".toCharArray();
    Random r = new Random();
    int[] powers = new int[10];

    {
        for (int i = 0; i < powers.length; ++i) {
            powers[i] = (int) Math.pow(digLetters.length, i);
        }
    }

    private BitSet[] bits = new BitSet[16];

    private File file = new File("/Users/wenshao/Downloads/fnv/hash.bin");
    FileOutputStream out;

    protected void setUp() throws Exception {
        out = new FileOutputStream(file);

        for (int i = 0; i < bits.length; ++i) {
            bits[i] = new BitSet(Integer.MAX_VALUE);
        }
    }

    protected void tearDown() throws Exception {
        out.close();
    }

    public void test_fnv_hash() throws Exception {
        int collisionCount = 0;

        long id_hash_64 = fnv_hash("name".toCharArray());
        int id_hash_32 = Math.abs((int) id_hash_64);
        //bitset.set(id_hash_32);

        long v = 0;
        long time = System.currentTimeMillis();
        NumberFormat format = NumberFormat.getInstance();

        byte[] b = new byte[8];
        for (int len = 1; len <= 5; ++len){
            char[] chars = new char[len];
            long n = (long) Math.pow(digLetters.length, chars.length);

            for (; v < n; ++v) {
                long hash = 0x811c9dc5;
                for (int i = 0; i < chars.length; ++i) {
                    int power = powers[chars.length - i - 1];
                    int d = (int) ((v / power) % digLetters.length);
                    char c = digLetters[d];

                    hash ^= c;
                    hash *= 0x1000193;
                }
                b[7] = (byte) (hash       );
                b[6] = (byte) (hash >>>  8);
                b[5] = (byte) (hash >>> 16);
                b[4] = (byte) (hash >>> 24);
                b[3] = (byte) (hash >>> 32);
                b[2] = (byte) (hash >>> 40);
                b[1] = (byte) (hash >>> 48);
                b[0] = (byte) (hash >>> 56);
                out.write(b);

                if (v != 0 && v % (1000 * 1000 * 10) == 0) {
                    long now = System.currentTimeMillis();
                    long millis = now - time;
                    time = now;
                    System.out.println("millis : " + millis + ", collision " + format.format(collisionCount) + ", " + format.format(v));
                }
            }

            System.out.println("end : " + len);
        }
    }

    String build(long v, int len) {
        char[] chars = new char[len];
        for (int i = 0; i < chars.length; ++i) {
            int power = powers[chars.length - i - 1];
            int d = (int) ((v / power) % digLetters.length);
            chars[i] = digLetters[d];
        }

        return new String(chars);
    }

    static long fnv_hash(char[] chars) {
        long hash = 0x811c9dc5;
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            hash ^= c;
            hash *= 0x1000193;
        }
        return hash;
    }
}
