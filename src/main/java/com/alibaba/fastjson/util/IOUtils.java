/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.util;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

import com.alibaba.fastjson.JSONException;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class IOUtils {

    public final static char[]    DIGITS                     = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
            'B', 'C', 'D', 'E', 'F'                         };

    public final static boolean[] firstIdentifierFlags       = new boolean[256];
    static {
        for (char c = 0; c < firstIdentifierFlags.length; ++c) {
            if (c >= 'A' && c <= 'Z') {
                firstIdentifierFlags[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                firstIdentifierFlags[c] = true;
            } else if (c == '_') {
                firstIdentifierFlags[c] = true;
            }
        }
    }

    public final static boolean[] identifierFlags            = new boolean[256];

    static {
        for (char c = 0; c < identifierFlags.length; ++c) {
            if (c >= 'A' && c <= 'Z') {
                identifierFlags[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                identifierFlags[c] = true;
            } else if (c == '_') {
                identifierFlags[c] = true;
            } else if (c >= '0' && c <= '9') {
                identifierFlags[c] = true;
            }
        }
    }

    public final static byte[]    specicalFlags_doubleQuotes = new byte[256];
    public final static byte[]    specicalFlags_singleQuotes = new byte[256];

    public final static char[]    replaceChars               = new char[128];
    static {
        specicalFlags_doubleQuotes['\0'] = 4;
        specicalFlags_doubleQuotes['\1'] = 4;
        specicalFlags_doubleQuotes['\2'] = 4;
        specicalFlags_doubleQuotes['\3'] = 4;
        specicalFlags_doubleQuotes['\4'] = 4;
        specicalFlags_doubleQuotes['\5'] = 4;
        specicalFlags_doubleQuotes['\6'] = 4;
        specicalFlags_doubleQuotes['\7'] = 4;
        specicalFlags_doubleQuotes['\b'] = 1; // 8
        specicalFlags_doubleQuotes['\t'] = 1; // 9
        specicalFlags_doubleQuotes['\n'] = 1; // 10
        specicalFlags_doubleQuotes['\u000B'] = 4; // 11
        specicalFlags_doubleQuotes['\f'] = 1;
        specicalFlags_doubleQuotes['\r'] = 1;
        specicalFlags_doubleQuotes['\"'] = 1;
        specicalFlags_doubleQuotes['\\'] = 1;

        specicalFlags_singleQuotes['\0'] = 4;
        specicalFlags_singleQuotes['\1'] = 4;
        specicalFlags_singleQuotes['\2'] = 4;
        specicalFlags_singleQuotes['\3'] = 4;
        specicalFlags_singleQuotes['\4'] = 4;
        specicalFlags_singleQuotes['\5'] = 4;
        specicalFlags_singleQuotes['\6'] = 4;
        specicalFlags_singleQuotes['\7'] = 4;
        specicalFlags_singleQuotes['\b'] = 1; // 8
        specicalFlags_singleQuotes['\t'] = 1; // 9
        specicalFlags_singleQuotes['\n'] = 1; // 10
        specicalFlags_singleQuotes['\u000B'] = 4; // 11
        specicalFlags_singleQuotes['\f'] = 1; // 12
        specicalFlags_singleQuotes['\r'] = 1; // 13
        specicalFlags_singleQuotes['\\'] = 1;
        specicalFlags_singleQuotes['\''] = 1;

        for (int i = 0x0E; i <= 0x1F; ++i) {
            specicalFlags_doubleQuotes[i] = 4;
            specicalFlags_singleQuotes[i] = 4;
        }

        for (int i = 0x7F; i <= 0xA0; ++i) {
            specicalFlags_doubleQuotes[i] = 4;
            specicalFlags_singleQuotes[i] = 4;
        }

        replaceChars['\0'] = '0';
        replaceChars['\1'] = '1';
        replaceChars['\2'] = '2';
        replaceChars['\3'] = '3';
        replaceChars['\4'] = '4';
        replaceChars['\5'] = '5';
        replaceChars['\6'] = '6';
        replaceChars['\7'] = '7';
        replaceChars['\b'] = 'b'; // 8
        replaceChars['\t'] = 't'; // 9
        replaceChars['\n'] = 'n'; // 10
        replaceChars['\u000B'] = 'v'; // 11
        replaceChars['\f'] = 'f'; // 12
        replaceChars['\r'] = 'r'; // 13
        replaceChars['\"'] = '"'; // 34
        replaceChars['\''] = '\''; // 39
        replaceChars['/'] = '/'; // 47
        replaceChars['\\'] = '\\'; // 92
    }

    public final static char[]    ASCII_CHARS                = { '0', '0', '0', '1', '0', '2', '0', '3', '0', '4', '0',
            '5', '0', '6', '0', '7', '0', '8', '0', '9', '0', 'A', '0', 'B', '0', 'C', '0', 'D', '0', 'E', '0', 'F',
            '1', '0', '1', '1', '1', '2', '1', '3', '1', '4', '1', '5', '1', '6', '1', '7', '1', '8', '1', '9', '1',
            'A', '1', 'B', '1', 'C', '1', 'D', '1', 'E', '1', 'F', '2', '0', '2', '1', '2', '2', '2', '3', '2', '4',
            '2', '5', '2', '6', '2', '7', '2', '8', '2', '9', '2', 'A', '2', 'B', '2', 'C', '2', 'D', '2', 'E', '2',
            'F',                                            };

    public static void close(Closeable x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception e) {
                // skip
            }
        }
    }

    // Requires positive x
    public static int stringSize(long x) {
        long p = 10;
        for (int i = 1; i < 19; i++) {
            if (x < p) return i;
            p = 10 * p;
        }
        return 19;
    }

    public static void getChars(long i, int index, char[] buf) {
        long q;
        int r;
        int charPos = index;
        char sign = 0;

        if (i < 0) {
            sign = '-';
            i = -i;
        }

        // Get 2 digits/iteration using longs until quotient fits into an int
        while (i > Integer.MAX_VALUE) {
            q = i / 100;
            // really: r = i - (q * 100);
            r = (int) (i - ((q << 6) + (q << 5) + (q << 2)));
            i = q;
            buf[--charPos] = DigitOnes[r];
            buf[--charPos] = DigitTens[r];
        }

        // Get 2 digits/iteration using ints
        int q2;
        int i2 = (int) i;
        while (i2 >= 65536) {
            q2 = i2 / 100;
            // really: r = i2 - (q * 100);
            r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
            i2 = q2;
            buf[--charPos] = DigitOnes[r];
            buf[--charPos] = DigitTens[r];
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i2 <= 65536, i2);
        for (;;) {
            q2 = (i2 * 52429) >>> (16 + 3);
            r = i2 - ((q2 << 3) + (q2 << 1)); // r = i2-(q2*10) ...
            buf[--charPos] = digits[r];
            i2 = q2;
            if (i2 == 0) break;
        }
        if (sign != 0) {
            buf[--charPos] = sign;
        }
    }

    /**
     * Places characters representing the integer i into the character array buf. The characters are placed into the
     * buffer backwards starting with the least significant digit at the specified index (exclusive), and working
     * backwards from there. Will fail if i == Integer.MIN_VALUE
     */
    public static void getChars(int i, int index, char[] buf) {
        int q, r;
        int charPos = index;
        char sign = 0;

        if (i < 0) {
            sign = '-';
            i = -i;
        }

        // Generate two digits per iteration
        while (i >= 65536) {
            q = i / 100;
            // really: r = i - (q * 100);
            r = i - ((q << 6) + (q << 5) + (q << 2));
            i = q;
            buf[--charPos] = DigitOnes[r];
            buf[--charPos] = DigitTens[r];
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i <= 65536, i);
        for (;;) {
            q = (i * 52429) >>> (16 + 3);
            r = i - ((q << 3) + (q << 1)); // r = i-(q*10) ...
            buf[--charPos] = digits[r];
            i = q;
            if (i == 0) break;
        }
        if (sign != 0) {
            buf[--charPos] = sign;
        }
    }

    public static void getChars(byte b, int index, char[] buf) {
        int i = b;
        int q, r;
        int charPos = index;
        char sign = 0;

        if (i < 0) {
            sign = '-';
            i = -i;
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i <= 65536, i);
        for (;;) {
            q = (i * 52429) >>> (16 + 3);
            r = i - ((q << 3) + (q << 1)); // r = i-(q*10) ...
            buf[--charPos] = digits[r];
            i = q;
            if (i == 0) break;
        }
        if (sign != 0) {
            buf[--charPos] = sign;
        }
    }

    /**
     * All possible chars for representing a number as a String
     */
    final static char[] digits    = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    final static char[] DigitTens = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1',
            '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3',
            '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5',
            '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7',
            '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', };

    final static char[] DigitOnes = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', };

    final static int[]  sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE };

    // Requires positive x
    public static int stringSize(int x) {
        for (int i = 0;; i++) {
            if (x <= sizeTable[i]) {
                return i + 1;
            }
        }
    }

    public static void decode(CharsetDecoder charsetDecoder, ByteBuffer byteBuf, CharBuffer charByte) {
        try {
            CoderResult cr = charsetDecoder.decode(byteBuf, charByte, true);

            if (!cr.isUnderflow()) {
                cr.throwException();
            }

            cr = charsetDecoder.flush(charByte);

            if (!cr.isUnderflow()) {
                cr.throwException();
            }
        } catch (CharacterCodingException x) {
            // Substitution is always enabled,
            // so this shouldn't happen
            throw new JSONException(x.getMessage(), x);
        }
    }

    public static boolean[] whitespaceFlags = new boolean[256];
    static {
        whitespaceFlags[' '] = true;
        whitespaceFlags['\n'] = true;
        whitespaceFlags['\r'] = true;
        whitespaceFlags['\t'] = true;
        whitespaceFlags['\f'] = true;
        whitespaceFlags['\b'] = true;
    }
    
    public static boolean firstIdentifier(char ch) {
        return ch < IOUtils.firstIdentifierFlags.length && IOUtils.firstIdentifierFlags[ch];
    }
    
    public static boolean isIdent(char ch) {
        return ch < identifierFlags.length && identifierFlags[ch];
    }
}
