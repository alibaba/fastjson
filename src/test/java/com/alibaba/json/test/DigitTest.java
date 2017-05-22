/*
 * Copyright 1999-2017 Alibaba Group.
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
package com.alibaba.json.test;

import java.text.NumberFormat;
import java.util.BitSet;

import junit.framework.TestCase;

public class DigitTest extends TestCase {

    private char[] text  = "[-5.041598256063065E-20,-7210028408342716000]".toCharArray();
    private int    COUNT = 1000 * 1000;

    public void test_perf() throws Exception {
        for (int i = 0; i < 50; ++i) {
            f_isDigitBitSet();
            f_isDigitArray();
            f_isDigitRange();
            f_isDigitSwitch();
            f_isDigitProhibit();

            System.out.println();
            System.out.println();
        }
    }

    public void f_isDigitBitSet() throws Exception {
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            for (char ch : text) {
                isDigitBitSet(ch);
            }
        }
        long nano = System.nanoTime() - startNano;
        System.out.println("bitset \t: " + NumberFormat.getInstance().format(nano));
    }

    public void f_isDigitRange() throws Exception {
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            for (char ch : text) {
                isDigitRange(ch);
            }
        }
        long nano = System.nanoTime() - startNano;
        System.out.println("range \t: " + NumberFormat.getInstance().format(nano));
    }

    public void f_isDigitArray() throws Exception {
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            for (char ch : text) {
                isDigitArray(ch);
            }
        }
        long nano = System.nanoTime() - startNano;
        System.out.println("array \t: " + NumberFormat.getInstance().format(nano));
    }

    public void f_isDigitSwitch() throws Exception {
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            for (char ch : text) {
                isDigitSwitch(ch);
            }
        }
        long nano = System.nanoTime() - startNano;
        System.out.println("swtich \t: " + NumberFormat.getInstance().format(nano));
    }

    public void f_isDigitProhibit() throws Exception {
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            for (char ch : text) {
                isDigitProhibit(ch);
            }
        }
        long nano = System.nanoTime() - startNano;
        System.out.println("prohi \t: " + NumberFormat.getInstance().format(nano));
    }

    private static final boolean[] digitBits = new boolean[256];
    static {
        for (char ch = '0'; ch <= '9'; ++ch) {
            digitBits[ch] = true;
        }
    }

    public final boolean isDigitArray(char ch) {
        return digitBits[ch];
    }

    private static final DetectProhibitChar digitDetectProhibitChar = new DetectProhibitChar(new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' });

    public final boolean isDigitProhibit(char ch) {
        return digitDetectProhibitChar.isProhibitChar(ch);
    }

    public final boolean isDigitRange(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private static final BitSet bits = new BitSet();
    static {
        for (char ch = '0'; ch <= '9'; ++ch) {
            bits.set(ch, true);
        }
    }

    public final boolean isDigitBitSet(char ch) {
        return bits.get(ch);
    }

    private final boolean isDigitSwitch(char ch) {
        switch (ch) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;
            default:
                return false;
        }
    }
}
