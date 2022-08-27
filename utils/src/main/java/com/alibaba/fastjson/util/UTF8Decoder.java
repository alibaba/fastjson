package com.alibaba.fastjson.util;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/*  Legal UTF-8 Byte Sequences
 *
 * #    Code Points      Bits   Bit/Byte pattern
 * 1                     7      0xxxxxxx
 *      U+0000..U+007F          00..7F
 *
 * 2                     11     110xxxxx    10xxxxxx
 *      U+0080..U+07FF          C2..DF      80..BF
 *
 * 3                     16     1110xxxx    10xxxxxx    10xxxxxx
 *      U+0800..U+0FFF          E0          A0..BF      80..BF
 *      U+1000..U+FFFF          E1..EF      80..BF      80..BF
 *
 * 4                     21     11110xxx    10xxxxxx    10xxxxxx    10xxxxxx
 *     U+10000..U+3FFFF         F0          90..BF      80..BF      80..BF
 *     U+40000..U+FFFFF         F1..F3      80..BF      80..BF      80..BF
 *    U+100000..U10FFFF         F4          80..8F      80..BF      80..BF
 *
 */

/**
 * @deprecated
 */
public class UTF8Decoder extends CharsetDecoder {

    private final static Charset charset = Charset.forName("UTF-8");

    public UTF8Decoder(){
        super(charset, 1.0f, 1.0f);
    }

    private static boolean isNotContinuation(int b) {
        return (b & 0xc0) != 0x80;
    }

    // [C2..DF] [80..BF]
    private static boolean isMalformed2(int b1, int b2) {
        return (b1 & 0x1e) == 0x0 || (b2 & 0xc0) != 0x80;
    }

    // [E0] [A0..BF] [80..BF]
    // [E1..EF] [80..BF] [80..BF]
    private static boolean isMalformed3(int b1, int b2, int b3) {
        return (b1 == (byte) 0xe0 && (b2 & 0xe0) == 0x80) || (b2 & 0xc0) != 0x80 || (b3 & 0xc0) != 0x80;
    }

    // [F0] [90..BF] [80..BF] [80..BF]
    // [F1..F3] [80..BF] [80..BF] [80..BF]
    // [F4] [80..8F] [80..BF] [80..BF]
    // only check 80-be range here, the [0xf0,0x80...] and [0xf4,0x90-...]
    // will be checked by Surrogate.neededFor(uc)
    private static boolean isMalformed4(int b2, int b3, int b4) {
        return (b2 & 0xc0) != 0x80 || (b3 & 0xc0) != 0x80 || (b4 & 0xc0) != 0x80;
    }

    private static CoderResult lookupN(ByteBuffer src, int n) {
        for (int i = 1; i < n; i++) {
            if (isNotContinuation(src.get())) return CoderResult.malformedForLength(i);
        }
        return CoderResult.malformedForLength(n);
    }

    public static CoderResult malformedN(ByteBuffer src, int nb) {
        switch (nb) {
            case 1:
                int b1 = src.get();
                if ((b1 >> 2) == -2) {
                    // 5 bytes 111110xx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
                    if (src.remaining() < 4) return CoderResult.UNDERFLOW;
                    return lookupN(src, 5);
                }
                if ((b1 >> 1) == -2) {
                    // 6 bytes 1111110x 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
                    if (src.remaining() < 5) {
                        return CoderResult.UNDERFLOW;
                    }
                    return lookupN(src, 6);
                }
                return CoderResult.malformedForLength(1);
            case 2: // always 1
                return CoderResult.malformedForLength(1);
            case 3:
                b1 = src.get();
                int b2 = src.get(); // no need to lookup b3
                return CoderResult.malformedForLength(((b1 == (byte) 0xe0 && (b2 & 0xe0) == 0x80) || isNotContinuation(b2)) ? 1 : 2);
            case 4: // we don't care the speed here
                b1 = src.get() & 0xff;
                b2 = src.get() & 0xff;
                if (b1 > 0xf4 || (b1 == 0xf0 && (b2 < 0x90 || b2 > 0xbf)) || (b1 == 0xf4 && (b2 & 0xf0) != 0x80) || isNotContinuation(b2)) return CoderResult.malformedForLength(1);
                if (isNotContinuation(src.get())) return CoderResult.malformedForLength(2);
                return CoderResult.malformedForLength(3);
            default:
                throw new IllegalStateException();
        }
    }

    private static CoderResult malformed(ByteBuffer src, int sp, CharBuffer dst, int dp, int nb) {
        src.position(sp - src.arrayOffset());
        CoderResult cr = malformedN(src, nb);
        src.position(sp);
        dst.position(dp);
        return cr;
    }

    private static CoderResult xflow(Buffer src, int sp, int sl, Buffer dst, int dp, int nb) {
        src.position(sp);
        dst.position(dp);
        return (nb == 0 || sl - sp < nb) ? CoderResult.UNDERFLOW : CoderResult.OVERFLOW;
    }

    private CoderResult decodeArrayLoop(ByteBuffer src, CharBuffer dst) {
        // This method is optimized for ASCII input.
        byte[] srcArray = src.array();
        int srcPosition = src.arrayOffset() + src.position();
        int srcLength = src.arrayOffset() + src.limit();

        char[] destArray = dst.array();
        int destPosition = dst.arrayOffset() + dst.position();
        int destLength = dst.arrayOffset() + dst.limit();
        int destLengthASCII = destPosition + Math.min(srcLength - srcPosition, destLength - destPosition);

        // ASCII only loop
        while (destPosition < destLengthASCII && srcArray[srcPosition] >= 0) {
            destArray[destPosition++] = (char) srcArray[srcPosition++];
        }

        while (srcPosition < srcLength) {
            int b1 = srcArray[srcPosition];
            if (b1 >= 0) {
                // 1 byte, 7 bits: 0xxxxxxx
                if (destPosition >= destLength) {
                    return xflow(src, srcPosition, srcLength, dst, destPosition, 1);
                }
                destArray[destPosition++] = (char) b1;
                srcPosition++;
            } else if ((b1 >> 5) == -2) {
                // 2 bytes, 11 bits: 110xxxxx 10xxxxxx
                if (srcLength - srcPosition < 2 || destPosition >= destLength) {
                    return xflow(src, srcPosition, srcLength, dst, destPosition, 2);
                }
                int b2 = srcArray[srcPosition + 1];
                if (isMalformed2(b1, b2)) {
                    return malformed(src, srcPosition, dst, destPosition, 2);
                }
                destArray[destPosition++] = (char) (((b1 << 6) ^ b2) ^ 0x0f80);
                srcPosition += 2;
            } else if ((b1 >> 4) == -2) {
                // 3 bytes, 16 bits: 1110xxxx 10xxxxxx 10xxxxxx
                if (srcLength - srcPosition < 3 || destPosition >= destLength) {
                    return xflow(src, srcPosition, srcLength, dst, destPosition, 3);
                }
                int b2 = srcArray[srcPosition + 1];
                int b3 = srcArray[srcPosition + 2];
                if (isMalformed3(b1, b2, b3)) {
                    return malformed(src, srcPosition, dst, destPosition, 3);
                }
                destArray[destPosition++] = (char) (((b1 << 12) ^ (b2 << 6) ^ b3) ^ 0x1f80);
                srcPosition += 3;
            } else if ((b1 >> 3) == -2) {
                // 4 bytes, 21 bits: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
                if (srcLength - srcPosition < 4 || destLength - destPosition < 2) {
                    return xflow(src, srcPosition, srcLength, dst, destPosition, 4);
                }
                int b2 = srcArray[srcPosition + 1];
                int b3 = srcArray[srcPosition + 2];
                int b4 = srcArray[srcPosition + 3];
                int uc = ((b1 & 0x07) << 18) | ((b2 & 0x3f) << 12) | ((b3 & 0x3f) << 06) | (b4 & 0x3f);
                if (isMalformed4(b2, b3, b4) || !((uc >= 0x10000) && (uc <= 1114111))) {
                    return malformed(src, srcPosition, dst, destPosition, 4);
                }
                destArray[destPosition++] = (char) (0xd800 | (((uc - 0x10000) >> 10) & 0x3ff));
                destArray[destPosition++] = (char) (0xdc00 | ((uc - 0x10000) & 0x3ff));
                srcPosition += 4;
            } else {
                return malformed(src, srcPosition, dst, destPosition, 1);
            }
        }
        return xflow(src, srcPosition, srcLength, dst, destPosition, 0);
    }

    protected CoderResult decodeLoop(ByteBuffer src, CharBuffer dst) {
        return decodeArrayLoop(src, dst);
    }




}
