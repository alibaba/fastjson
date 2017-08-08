package com.alibaba.json;

import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by wenshao on 07/08/2017.
 */
public class X {
    private static String text = "Javaone KeynoteBill GatesSteve Jobshttp://javaone.com/keynote_large.jpgJavaone Keynotehttp://javaone.com/keynote_small.jpgJavaone Keynote";
    private static char[] chars = new StringBuffer().append(text).toString().toCharArray();
    public static void main(String[] args) throws Exception {
//        for (int i = 0; i < 127; ++i) {
//            if ((i & 96) != 0) {
//                print(i);
//            }
//        }
        int c = 1 + 2 + 4 + 8 + 16 + 32;
        System.out.println(Integer.toHexString(c));

        //c < 127 && c & 63

        // 1 2 4 8 16 32 64
        for (int i = 0; i < 10; ++i) {
            perf();
        }

    }

    static void perf() {
        long start = System.currentTimeMillis();

        int features = 0;

        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            int sepcialCount = 0;
            for (int j = 0; j < chars.length; ++j) {
                char ch = chars[j];
                if (isSpecial(ch, features)) {
                    sepcialCount++;
                }
            }
        }

        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    static boolean isSpecial(int ch, int features) {
        if (ch >= ']') {
            if (ch >= 0x7F //
                    && (ch == '\u2028' //
                    || ch == '\u2029' //
                    || ch < 0xA0)) {
                return true;
            }
            return false;
        }

        return (ch < 64 && (S2 & (1L << ch)) != 0) || ch == '\\';
    }

    static long X;
    static {
        for (int i = 0; i < 31;++i) {
            X |= (1L << i);
        }
        X |= (1L << '"'); // 34
        X |= (1L << '#'); //
    }
    static boolean isSpecial1(int ch, int features) {
        if (ch < 127) {
            if ((X & (1L <<(ch & 63))) != 0) {
                return true;
            }
        }
        return false;
    }



    final static long S0 = 0x4FFFFFFFFL, S1 = 0x8004FFFFFFFFL, S2 = 0x50008004FFFFFFFFL;

    static void print(int c) {
        System.out.print(Integer.toString(c) + " ");
        for (int i = 0; i < 8; ++i) {
            boolean t = (c & (1 << i)) != 0;
            System.out.print(t ? "1 " : "0 ");
        }

        if (c >= 32 && c < 127) {
            System.out.print(" " + (char) c);
        }

        System.out.println();
    }
}
