package com.alibaba.json.test;

import junit.framework.TestCase;

import java.text.NumberFormat;
import java.util.Random;

/**
 * Created by wenshao on 08/01/2017.
 */
public class FNV32_CollisionTest_2 extends TestCase {
    char[] digLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_".toCharArray();
    //char[] digLetters = "0123456789".toCharArray();
    Random r = new Random();
    int[] powers = new int[10];

    {
        for (int i = 0; i < powers.length; ++i) {
            powers[i] = (int) Math.pow(digLetters.length, i);
        }
    }


    public void test_fnv_hash_7() throws Exception {
        int COUNT = 1000 * 1000 * 1000;

        long id_hash_64 = fnv_hash("name".toCharArray());
        int id_hash_32 = (int) id_hash_64;
        System.out.println("name : " + id_hash_32 + ", " + id_hash_64);

        long v = 0;
        long time = System.currentTimeMillis();
        NumberFormat format = NumberFormat.getInstance();


        final int len = 7;
        char[] chars = new char[len];
        for (int i0 = 0; i0 < digLetters.length; ++i0) {
            long h0 = 0x811c9dc5;
            char c0 = digLetters[i0];
            h0 ^= c0;
            h0 *= 0x1000193;

            chars[0] = c0;
            for (int i1 = 0; i1 < digLetters.length; ++i1) {
                char c1 = digLetters[i1];
                chars[1] = c1;

                long h1 = h0;
                h1 ^= c1;
                h1 *= 0x1000193;

                for (int i2 = 0; i2 < digLetters.length; ++i2) {
                    char c2 = digLetters[i2];

                    long h2 = h1;
                    h2 ^= c2;
                    h2 *= 0x1000193;

                    chars[2] = c2;
                    for (int i3 = 0; i3 < digLetters.length; ++i3) {
                        char c3 = digLetters[i3];

                        long h3 = h2;
                        h3 ^= c3;
                        h3 *= 0x1000193;

                        chars[3] = c3;
                        for (int i4 = 0; i4 < digLetters.length; ++i4) {
                            char c4 = digLetters[i4];

                            long h4 = h3;
                            h4 ^= c4;
                            h4 *= 0x1000193;

                            chars[4] = c4;
                            for (int i5 = 0; i5 < digLetters.length; ++i5) {
                                char c5 = digLetters[i5];
                                chars[5] = c5;

                                long h5 = h4;
                                h5 ^= c5;
                                h5 *= 0x1000193;

                                for (int i6 = 0; i6 < digLetters.length; ++i6) {
                                    char c6 = digLetters[i6];

                                    long h6 = h5;
                                    h6 ^= c6;
                                    h6 *= 0x1000193;

                                    chars[6] = c6;

                                    v++;
                                    if (h6 == id_hash_64) {
                                        int hash_32 = (int) h6;
                                        System.out.println("collision : " + build(v, len) + "， hash64 : " + h6 + ", hash 32 " + hash_32);
                                        break;
                                    }

                                    if (v != 0 && v % (1000 * 1000 * 1000) == 0) {
                                        long now = System.currentTimeMillis();
                                        long millis = now - time;
                                        time = now;
                                        System.out.println("millis : " + millis + ", " + format.format(v));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        System.out.println("end : " + len);

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
