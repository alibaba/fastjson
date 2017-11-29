package com.alibaba.json.test;

import junit.framework.TestCase;

import java.util.*;

/**
 * Created by wenshao on 05/01/2017.
 */
public class FNVHashTest extends TestCase {
    char[] digLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_".toCharArray();
    Random r = new Random();

    public void test_fnv_hash() throws Exception {
        int COUNT = 1000 * 1000 * 1000;

        int collision_cnt = 0;
        // Map<Long, char[]> map = new HashMap<Long, char[]>(COUNT);

        long id_hash = fnv_hash("id".toCharArray());
        System.out.printf("id : " + id_hash);
        System.out.println();
        for (int i = 0; i < digLetters.length; ++i) {
            System.out.print(digLetters[i]);
            System.out.print(",");
        }
//        for (int i = 0; i < COUNT; ++i) {
//            char[] chars = gen();
//            int hash = fnv_hash32(chars);
//            if (hash == id_hash) {
//                System.out.println(new String(chars));
//                break;
//            }
//        }

//        for (int i = 0; i < COUNT; ++i) {
//            char[] chars = gen();
//            Long hash = bkdr_hash(chars);
//
//            char[] chars_2 = map.get(hash);
//            if (chars_2 != null) {
//                if (!Arrays.equals(chars, chars_2)) {
//                    System.out.println("collision (" + collision_cnt++ + ") : " + new String(chars) + " -> " + new String(chars_2));
//                }
//            } else {
//                map.put(hash, chars);
//            }
//        }
    }

    private char[] gen() {
        int len = r.nextInt(32);
        char[] chars = new char[len];
        for (int i = 0; i < chars.length; ++i) {
            chars[i] = digLetters[r.nextInt(digLetters.length)];
        }
        return chars;
    }

    static int fnv_hash32(char[] chars) {
        long hash = 0x811c9dc5;
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            hash ^= c;
            hash *= 0x1000193;
        }
        return (int) hash;
    }

    static long fnv_hash64(char[] chars) {
        long hash = 0xcbf29ce484222325L;
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            hash ^= c;
            hash *= 0x100000001b3L;
        }
        return hash;
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

    static long bkdr_hash(char[] chars) {
        long hash = 0;
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            hash = hash * 131 + c;
        }
        return hash;
    }
}
