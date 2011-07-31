package com.alibaba.fastjson.util;

public class ThreadLocalCache {

    public final static int                  CHARS_CACH_INIT_SIZE = 1024;                     // 1k;
    public final static int                  CHARS_CACH_MAX_SIZE  = 1024 * 128;               // 1k;
    private final static ThreadLocal<char[]> charsBufLocal        = new ThreadLocal<char[]>();

    public static void clearChars() {
        charsBufLocal.set(null);
    }

    public static char[] getChars(int length) {
        char[] chars = charsBufLocal.get();

        if (chars == null) {
            chars = allocate(length);

            return chars;
        }

        if (chars.length < length) {
            chars = allocate(length);
        }

        return chars;
    }

    private static char[] allocate(int length) {
        int allocateLength = getAllocateLength(CHARS_CACH_INIT_SIZE, CHARS_CACH_MAX_SIZE, length);

        if (allocateLength <= CHARS_CACH_MAX_SIZE) {
            char[] chars = new char[allocateLength];
            charsBufLocal.set(chars);
            return chars;
        }

        return new char[length];
    }

    private static int getAllocateLength(int init, int max, int length) {
        int value = init;
        for (;;) {
            if (value >= length) {
                return value;
            }

            value *= 2;

            if (value > max) {
                break;
            }
        }

        return length;
    }

    // /////////
    public final static int                  BYTES_CACH_INIT_SIZE = 1024;                     // 1k;
    public final static int                  BYTeS_CACH_MAX_SIZE  = 1024 * 128;               // 1k;
    private final static ThreadLocal<byte[]> bytesBufLocal        = new ThreadLocal<byte[]>();

    public static void clearBytes() {
        bytesBufLocal.set(null);
    }

    public static byte[] getBytes(int length) {
        byte[] bytes = bytesBufLocal.get();

        if (bytes == null) {
            bytes = allocateBytes(length);

            return bytes;
        }

        if (bytes.length < length) {
            bytes = allocateBytes(length);
        }

        return bytes;
    }

    private static byte[] allocateBytes(int length) {
        int allocateLength = getAllocateLength(CHARS_CACH_INIT_SIZE, CHARS_CACH_MAX_SIZE, length);

        if (allocateLength <= CHARS_CACH_MAX_SIZE) {
            byte[] chars = new byte[allocateLength];
            bytesBufLocal.set(chars);
            return chars;
        }

        return new byte[length];
    }

}
