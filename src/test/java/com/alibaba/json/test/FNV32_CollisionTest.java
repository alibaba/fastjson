package com.alibaba.json.test;

import junit.framework.TestCase;

import java.text.NumberFormat;
import java.util.Random;

/**
 * Created by wenshao on 08/01/2017.
 */
public class FNV32_CollisionTest extends TestCase {
    char[] digLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_".toCharArray();
    //char[] digLetters = "0123456789".toCharArray();
    Random r = new Random();
    int[] powers = new int[10];

    {
        for (int i = 0; i < powers.length; ++i) {
            powers[i] = (int) Math.pow(digLetters.length, i);
        }
    }


    public void test_fnv_hash() throws Exception {
        int COUNT = 1000 * 1000 * 1000;

        long id_hash_64 = fnv_hash("name".toCharArray());
        int id_hash_32 = (int) id_hash_64;
        System.out.println("name : " + id_hash_32 + ", " + id_hash_64);

        long v = 0;
        long time = System.currentTimeMillis();
        NumberFormat format = NumberFormat.getInstance();

        for (int len = 1; len <= 7; ++len){
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

                if (hash == id_hash_64) {
                    int hash_32 = (int) hash;
                    System.out.println("collision : " + build(v, len) + "， hash64 : " + hash + ", hash 32 " + hash_32);
                    break;
                }

                if (v != 0 && v % (1000 * 1000 * 100) == 0) {
                    long now = System.currentTimeMillis();
                    long millis = now - time;
                    time = now;
                    System.out.println("millis : " + millis + ", " + format.format(v));
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

    static long hash(char[] chars) {
        long hash = 0;
        for (int i = 0; i < chars.length; ++i) {
            hash = 31 * hash + chars[i];
        }
        return hash;
    }
}
