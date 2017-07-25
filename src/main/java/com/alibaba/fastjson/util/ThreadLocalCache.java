package com.alibaba.fastjson.util;

import java.lang.ref.SoftReference;
import java.nio.charset.CharsetDecoder;

/**
 * @deprecated
 */
public class ThreadLocalCache {

    public final static int                                 CHARS_CACH_INIT_SIZE = 1024;                                    // 1k, 2^10;
    public final static int 								CHARS_CACH_INIT_SIZE_EXP = 10;
    public final static int                                 CHARS_CACH_MAX_SIZE  = 1024 * 128;                              // 128k, 2^17;
    public final static int 								CHARS_CACH_MAX_SIZE_EXP = 17;
    private final static ThreadLocal<SoftReference<char[]>> charsBufLocal        = new ThreadLocal<SoftReference<char[]>>();

    private final static ThreadLocal<CharsetDecoder>        decoderLocal         = new ThreadLocal<CharsetDecoder>();

    public static CharsetDecoder getUTF8Decoder() {
        CharsetDecoder decoder = decoderLocal.get();
        if (decoder == null) {
            decoder = new UTF8Decoder();
            decoderLocal.set(decoder);
        }
        return decoder;
    }

    public static void clearChars() {
        charsBufLocal.set(null);
    }

    public static char[] getChars(int length) {
        SoftReference<char[]> ref = charsBufLocal.get();

        if (ref == null) {
            return allocate(length);
        }

        char[] chars = ref.get();

        if (chars == null) {
            return allocate(length);
        }

        if (chars.length < length) {
            chars = allocate(length);
        }

        return chars;
    }

    private static char[] allocate(int length) {
        if(length> CHARS_CACH_MAX_SIZE) {
            return new char[length];
        }

        int allocateLength = getAllocateLengthExp(CHARS_CACH_INIT_SIZE_EXP, CHARS_CACH_MAX_SIZE_EXP, length);
        char[] chars = new char[allocateLength];
        charsBufLocal.set(new SoftReference<char[]>(chars));
        return chars;
    }

    private static int getAllocateLengthExp(int minExp, int maxExp, int length) {
        assert (1<<maxExp) >= length;
//		int max = 1 << maxExp;
//		if(length>= max) {
//			return length;
//		}
        int part = length >>> minExp;
        if(part <= 0) {
            return 1<< minExp;
        }
        return 1 << 32 - Integer.numberOfLeadingZeros(length-1);
    }

    // /////////
    public final static int                                 BYTES_CACH_INIT_SIZE = 1024;                                    // 1k, 2^10;
    public final static int 								BYTES_CACH_INIT_SIZE_EXP = 10;
    public final static int                                 BYTES_CACH_MAX_SIZE  = 1024 * 128;                              // 128k, 2^17;
    public final static int 								BYTES_CACH_MAX_SIZE_EXP = 17;
    private final static ThreadLocal<SoftReference<byte[]>> bytesBufLocal        = new ThreadLocal<SoftReference<byte[]>>();

    public static void clearBytes() {
        bytesBufLocal.set(null);
    }

    public static byte[] getBytes(int length) {
        SoftReference<byte[]> ref = bytesBufLocal.get();

        if (ref == null) {
            return allocateBytes(length);
        }

        byte[] bytes = ref.get();

        if (bytes == null) {
            return allocateBytes(length);
        }

        if (bytes.length < length) {
            bytes = allocateBytes(length);
        }

        return bytes;
    }

    private static byte[] allocateBytes(int length) {
        if(length > BYTES_CACH_MAX_SIZE) {
            return new byte[length];
        }

        int allocateLength = getAllocateLengthExp(BYTES_CACH_INIT_SIZE_EXP, BYTES_CACH_MAX_SIZE_EXP, length);
        byte[] chars = new byte[allocateLength];
        bytesBufLocal.set(new SoftReference<byte[]>(chars));
        return chars;
    }

}