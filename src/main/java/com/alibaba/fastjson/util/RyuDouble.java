// Copyright 2018 Ulf Adams
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.alibaba.fastjson.util;

import java.math.BigInteger;

/**
 * An implementation of Ryu for double.
 */
public final class RyuDouble {
    private static final int[][] POW5_SPLIT = new int[326][4];
    private static final int[][] POW5_INV_SPLIT = new int[291][4];

    static {
        BigInteger mask = BigInteger.ONE.shiftLeft(31).subtract(BigInteger.ONE);
        BigInteger invMask = BigInteger.ONE.shiftLeft(31).subtract(BigInteger.ONE);
        for (int i = 0; i < 326; i++) {
            BigInteger pow = BigInteger.valueOf(5).pow(i);
            int pow5len = pow.bitLength();
            int expectedPow5Bits = i == 0 ? 1 : (int) ((i * 23219280L + 10000000L - 1) / 10000000L);
            if (expectedPow5Bits != pow5len) {
                throw new IllegalStateException(pow5len + " != " + expectedPow5Bits);
            }
            if (i < POW5_SPLIT.length) {
                for (int j = 0; j < 4; j++) {
                    POW5_SPLIT[i][j] = pow
                            .shiftRight(pow5len - 121 + (3 - j) * 31)
                            .and(mask)
                            .intValue();
                }
            }

            if (i < POW5_INV_SPLIT.length) {
                // We want floor(log_2 5^q) here, which is pow5len - 1.
                int j = pow5len + 121;
                BigInteger inv = BigInteger.ONE
                        .shiftLeft(j)
                        .divide(pow)
                        .add(BigInteger.ONE);
                for (int k = 0; k < 4; k++) {
                    if (k == 0) {
                        POW5_INV_SPLIT[i][k] = inv
                                .shiftRight((3 - k) * 31)
                                .intValue();
                    } else {
                        POW5_INV_SPLIT[i][k] = inv
                                .shiftRight((3 - k) * 31)
                                .and(invMask)
                                .intValue();
                    }
                }
            }
        }
    }

    public static String toString(double value) {
        char[] result = new char[24];
        int len = toString(value, result, 0);
        return new String(result, 0, len);
    }

    public static int toString(double value, char[] result, int off) {
        final long DOUBLE_MANTISSA_MASK = 4503599627370495L; // (1L << 52) - 1;
        final int DOUBLE_EXPONENT_MASK = 2047; // (1 << 11) - 1;
        final int DOUBLE_EXPONENT_BIAS = 1023; // (1 << (11 - 1)) - 1;
        final long LOG10_5_NUMERATOR = 6989700L; // (long) (10000000L * Math.log10(5));
        final long LOG10_2_NUMERATOR = 3010299L; // (long) (10000000L * Math.log10(2));

        // Step 1: Decode the floating point number, and unify normalized and subnormal cases.
        // First, handle all the trivial cases.
        int index = off;
        if (Double.isNaN(value)) {
            result[index++] = 'N';
            result[index++] = 'a';
            result[index++] = 'N';
            return index - off;
        }

        if (value == Double.POSITIVE_INFINITY) {
            result[index++] = 'I';
            result[index++] = 'n';
            result[index++] = 'f';
            result[index++] = 'i';
            result[index++] = 'n';
            result[index++] = 'i';
            result[index++] = 't';
            result[index++] = 'y';
            return index - off;
        }

        if (value == Double.NEGATIVE_INFINITY) {
            result[index++] = '-';
            result[index++] = 'I';
            result[index++] = 'n';
            result[index++] = 'f';
            result[index++] = 'i';
            result[index++] = 'n';
            result[index++] = 'i';
            result[index++] = 't';
            result[index++] = 'y';
            return index - off;
        }

        long bits = Double.doubleToLongBits(value);
        if (bits == 0) {
            result[index++] = '0';
            result[index++] = '.';
            result[index++] = '0';
            return index - off;
        }
        if (bits == 0x8000000000000000L) {
            result[index++] = '-';
            result[index++] = '0';
            result[index++] = '.';
            result[index++] = '0';
            return index - off;
        }

        final int DOUBLE_MANTISSA_BITS = 52;
        // Otherwise extract the mantissa and exponent bits and run the full algorithm.
        int ieeeExponent = (int) ((bits >>> DOUBLE_MANTISSA_BITS) & DOUBLE_EXPONENT_MASK);
        long ieeeMantissa = bits & DOUBLE_MANTISSA_MASK;
        int e2;
        long m2;
        if (ieeeExponent == 0) {
            // Denormal number - no implicit leading 1, and the exponent is 1, not 0.
            e2 = 1 - DOUBLE_EXPONENT_BIAS - DOUBLE_MANTISSA_BITS;
            m2 = ieeeMantissa;
        } else {
            // Add implicit leading 1.
            e2 = ieeeExponent - DOUBLE_EXPONENT_BIAS - DOUBLE_MANTISSA_BITS;
            m2 = ieeeMantissa | (1L << DOUBLE_MANTISSA_BITS);
        }

        boolean sign = bits < 0;

        // Step 2: Determine the interval of legal decimal representations.
        boolean even = (m2 & 1) == 0;
        final long mv = 4 * m2;
        final long mp = 4 * m2 + 2;
        final int mmShift = ((m2 != (1L << DOUBLE_MANTISSA_BITS)) || (ieeeExponent <= 1)) ? 1 : 0;
        final long mm = 4 * m2 - 1 - mmShift;
        e2 -= 2;

        // Step 3: Convert to a decimal power base using 128-bit arithmetic.
        // -1077 = 1 - 1023 - 53 - 2 <= e_2 - 2 <= 2046 - 1023 - 53 - 2 = 968
        long dv, dp, dm;
        final int e10;
        boolean dmIsTrailingZeros = false, dvIsTrailingZeros = false;
        if (e2 >= 0) {
            final int q = Math.max(0, (int) (e2 * LOG10_2_NUMERATOR / 10000000L) - 1);
            // k = constant + floor(log_2(5^q))
            // q == 0 ? 1 : (int) ((q * 23219280L + 10000000L - 1) / 10000000L)
            final int k = 122 + (q == 0 ? 1 : (int) ((q * 23219280L + 10000000L - 1) / 10000000L)) - 1;
            final int i = -e2 + q + k;

            int actualShift = i - 3 * 31 - 21;
            if (actualShift < 0) {
                throw new IllegalArgumentException("" + actualShift);
            }

            final int[] ints = POW5_INV_SPLIT[q];
            {
                long mHigh = mv >>> 31;
                long mLow = mv & 0x7fffffff;
                long bits13 = mHigh * ints[0];
                long bits03 = mLow * ints[0];
                long bits12 = mHigh * ints[1];
                long bits02 = mLow * ints[1];
                long bits11 = mHigh * ints[2];
                long bits01 = mLow * ints[2];
                long bits10 = mHigh * ints[3];
                long bits00 = mLow * ints[3];


                dv = ((((((
                        ((bits00 >>> 31) + bits01 + bits10) >>> 31)
                        + bits02 + bits11) >>> 31)
                        + bits03 + bits12) >>> 21)
                        + (bits13 << 10)) >>> actualShift;
            }
            {
                long mHigh = mp >>> 31;
                long mLow = mp & 0x7fffffff;
                long bits13 = mHigh * ints[0];
                long bits03 = mLow * ints[0];
                long bits12 = mHigh * ints[1];
                long bits02 = mLow * ints[1];
                long bits11 = mHigh * ints[2];
                long bits01 = mLow * ints[2];
                long bits10 = mHigh * ints[3];
                long bits00 = mLow * ints[3];

                dp = ((((((
                        ((bits00 >>> 31) + bits01 + bits10) >>> 31)
                        + bits02 + bits11) >>> 31)
                        + bits03 + bits12) >>> 21)
                        + (bits13 << 10)) >>> actualShift;
            }
            {
                long mHigh = mm >>> 31;
                long mLow = mm & 0x7fffffff;
                long bits13 = mHigh * ints[0];
                long bits03 = mLow * ints[0];
                long bits12 = mHigh * ints[1];
                long bits02 = mLow * ints[1];
                long bits11 = mHigh * ints[2];
                long bits01 = mLow * ints[2];
                long bits10 = mHigh * ints[3];
                long bits00 = mLow * ints[3];

                dm = ((((((
                        ((bits00 >>> 31) + bits01 + bits10) >>> 31)
                        + bits02 + bits11) >>> 31)
                        + bits03 + bits12) >>> 21)
                        + (bits13 << 10)) >>> actualShift;
            }

            e10 = q;

            if (q <= 21) {
                if (mv % 5 == 0) {
                    int pow5Factor_mv;
                    {
                        long v = mv;
                        if ((v % 5) != 0) {
                            pow5Factor_mv = 0;
                        } else if ((v % 25) != 0) {
                            pow5Factor_mv = 1;
                        } else if ((v % 125) != 0) {
                            pow5Factor_mv = 2;
                        } else if ((v % 625) != 0) {
                            pow5Factor_mv = 3;
                        } else {
                            pow5Factor_mv = 4;
                            v /= 625;
                            while (v > 0) {
                                if (v % 5 != 0) {
                                    break;
                                }
                                v /= 5;
                                pow5Factor_mv++;
                            }
                        }
                    }
                    dvIsTrailingZeros = pow5Factor_mv >= q;
                } else if (even) {
                    int pow5Factor_mm;
                    {
                        long v = mm;
                        if ((v % 5) != 0) {
                            pow5Factor_mm = 0;
                        } else if ((v % 25) != 0) {
                            pow5Factor_mm = 1;
                        } else if ((v % 125) != 0) {
                            pow5Factor_mm = 2;
                        } else if ((v % 625) != 0) {
                            pow5Factor_mm = 3;
                        } else {
                            pow5Factor_mm = 4;
                            v /= 625;
                            while (v > 0) {
                                if (v % 5 != 0) {
                                    break;
                                }
                                v /= 5;
                                pow5Factor_mm++;
                            }
                        }
                    }

                    dmIsTrailingZeros = pow5Factor_mm >= q; //
                } else {
                    int pow5Factor_mp;
                    {
                        long v = mp;
                        if ((v % 5) != 0) {
                            pow5Factor_mp = 0;
                        } else if ((v % 25) != 0) {
                            pow5Factor_mp = 1;
                        } else if ((v % 125) != 0) {
                            pow5Factor_mp = 2;
                        } else if ((v % 625) != 0) {
                            pow5Factor_mp = 3;
                        } else {
                            pow5Factor_mp = 4;
                            v /= 625;
                            while (v > 0) {
                                if (v % 5 != 0) {
                                    break;
                                }
                                v /= 5;
                                pow5Factor_mp++;
                            }
                        }
                    }

                    if (pow5Factor_mp >= q) {
                        dp--;
                    }
                }
            }
        } else {
            final int q = Math.max(0, (int) (-e2 * LOG10_5_NUMERATOR / 10000000L) - 1);
            final int i = -e2 - q;
            final int k = (i == 0 ? 1 : (int) ((i * 23219280L + 10000000L - 1) / 10000000L)) - 121;
            final int j = q - k;

            int actualShift = j - 3 * 31 - 21;
            if (actualShift < 0) {
                throw new IllegalArgumentException("" + actualShift);
            }
            int[] ints = POW5_SPLIT[i];
            {
                long mHigh = mv >>> 31;
                long mLow = mv & 0x7fffffff;
                long bits13 = mHigh * ints[0]; // 124
                long bits03 = mLow * ints[0];  // 93
                long bits12 = mHigh * ints[1]; // 93
                long bits02 = mLow * ints[1];  // 62
                long bits11 = mHigh * ints[2]; // 62
                long bits01 = mLow * ints[2];  // 31
                long bits10 = mHigh * ints[3]; // 31
                long bits00 = mLow * ints[3];  // 0

                dv = ((((((
                        ((bits00 >>> 31) + bits01 + bits10) >>> 31)
                        + bits02 + bits11) >>> 31)
                        + bits03 + bits12) >>> 21)
                        + (bits13 << 10)) >>> actualShift;
            }
            {
                long mHigh = mp >>> 31;
                long mLow = mp & 0x7fffffff;
                long bits13 = mHigh * ints[0]; // 124
                long bits03 = mLow * ints[0];  // 93
                long bits12 = mHigh * ints[1]; // 93
                long bits02 = mLow * ints[1];  // 62
                long bits11 = mHigh * ints[2]; // 62
                long bits01 = mLow * ints[2];  // 31
                long bits10 = mHigh * ints[3]; // 31
                long bits00 = mLow * ints[3];  // 0
                dp = ((((((
                        ((bits00 >>> 31) + bits01 + bits10) >>> 31)
                        + bits02 + bits11) >>> 31)
                        + bits03 + bits12) >>> 21)
                        + (bits13 << 10)) >>> actualShift;
            }
            {
                long mHigh = mm >>> 31;
                long mLow = mm & 0x7fffffff;
                long bits13 = mHigh * ints[0]; // 124
                long bits03 = mLow * ints[0];  // 93
                long bits12 = mHigh * ints[1]; // 93
                long bits02 = mLow * ints[1];  // 62
                long bits11 = mHigh * ints[2]; // 62
                long bits01 = mLow * ints[2];  // 31
                long bits10 = mHigh * ints[3]; // 31
                long bits00 = mLow * ints[3];  // 0
                dm = ((((((
                        ((bits00 >>> 31) + bits01 + bits10) >>> 31)
                        + bits02 + bits11) >>> 31)
                        + bits03 + bits12) >>> 21)
                        + (bits13 << 10)) >>> actualShift;
            }

            e10 = q + e2;
            if (q <= 1) {
                dvIsTrailingZeros = true;
                if (even) {
                    dmIsTrailingZeros = mmShift == 1;
                } else {
                    dp--;
                }
            } else if (q < 63) {
                dvIsTrailingZeros = (mv & ((1L << (q - 1)) - 1)) == 0;
            }
        }

        // Step 4: Find the shortest decimal representation in the interval of legal representations.
        //
        // We do some extra work here in order to follow Float/Double.toString semantics. In particular,
        // that requires printing in scientific format if and only if the exponent is between -3 and 7,
        // and it requires printing at least two decimal digits.
        //
        // Above, we moved the decimal dot all the way to the right, so now we need to count digits to
        // figure out the correct exponent for scientific notation.
        final int vplength; //  = decimalLength(dp);
        if (dp >=        1000000000000000000L) {
            vplength= 19;
        } else if (dp >= 100000000000000000L) {
            vplength=  18;
        } else if (dp >= 10000000000000000L) {
            vplength = 17;
        } else if (dp >= 1000000000000000L) {
            vplength = 16;
        } else if (dp >= 100000000000000L) {
            vplength = 15;
        } else if (dp >= 10000000000000L) {
            vplength = 14;
        } else if (dp >= 1000000000000L) {
            vplength = 13;
        } else if (dp >= 100000000000L) {
            vplength = 12;
        } else if (dp >= 10000000000L) {
            vplength = 11;
        } else if (dp >= 1000000000L) {
            vplength = 10;
        } else if (dp >= 100000000L) {
            vplength = 9;
        } else if (dp >= 10000000L) {
            vplength = 8;
        } else if (dp >= 1000000L) {
            vplength = 7;
        } else if (dp >= 100000L) {
            vplength = 6;
        } else if (dp >= 10000L) {
            vplength = 5;
        } else if (dp >= 1000L) {
            vplength = 4;
        } else if (dp >= 100L) {
            vplength = 3;
        } else if (dp >= 10L) {
            vplength = 2;
        } else {
            vplength = 1;
        }

        int exp = e10 + vplength - 1;

        // Double.toString semantics requires using scientific notation if and only if outside this range.
        boolean scientificNotation = !((exp >= -3) && (exp < 7));

        int removed = 0;

        int lastRemovedDigit = 0;
        long output;
        if (dmIsTrailingZeros || dvIsTrailingZeros) {
            while (dp / 10 > dm / 10) {
                if ((dp < 100) && scientificNotation) {
                    // Double.toString semantics requires printing at least two digits.
                    break;
                }
                dmIsTrailingZeros &= dm % 10 == 0;
                dvIsTrailingZeros &= lastRemovedDigit == 0;
                lastRemovedDigit = (int) (dv % 10);
                dp /= 10;
                dv /= 10;
                dm /= 10;
                removed++;
            }
            if (dmIsTrailingZeros && even) {
                while (dm % 10 == 0) {
                    if ((dp < 100) && scientificNotation) {
                        // Double.toString semantics requires printing at least two digits.
                        break;
                    }
                    dvIsTrailingZeros &= lastRemovedDigit == 0;
                    lastRemovedDigit = (int) (dv % 10);
                    dp /= 10;
                    dv /= 10;
                    dm /= 10;
                    removed++;
                }
            }
            if (dvIsTrailingZeros && (lastRemovedDigit == 5) && (dv % 2 == 0)) {
                // Round even if the exact numbers is .....50..0.
                lastRemovedDigit = 4;
            }
            output = dv +
                    ((dv == dm && !(dmIsTrailingZeros && even)) || (lastRemovedDigit >= 5) ? 1 : 0);
        } else {
            while (dp / 10 > dm / 10) {
                if ((dp < 100) && scientificNotation) {
                    // Double.toString semantics requires printing at least two digits.
                    break;
                }
                lastRemovedDigit = (int) (dv % 10);
                dp /= 10;
                dv /= 10;
                dm /= 10;
                removed++;
            }
            output = dv + ((dv == dm || (lastRemovedDigit >= 5)) ? 1 : 0);
        }
        int olength = vplength - removed;

        // Step 5: Print the decimal representation.
        // We follow Double.toString semantics here.
        if (sign) {
            result[index++] = '-';
        }

        // Values in the interval [1E-3, 1E7) are special.
        if (scientificNotation) {
            // Print in the format x.xxxxxE-yy.
            for (int i = 0; i < olength - 1; i++) {
                int c = (int) (output % 10);
                output /= 10;
                result[index + olength - i] = (char) ('0' + c);
            }
            result[index] = (char) ('0' + output % 10);
            result[index + 1] = '.';
            index += olength + 1;
            if (olength == 1) {
                result[index++] = '0';
            }

            // Print 'E', the exponent sign, and the exponent, which has at most three digits.
            result[index++] = 'E';
            if (exp < 0) {
                result[index++] = '-';
                exp = -exp;
            }
            if (exp >= 100) {
                result[index++] = (char) ('0' + exp / 100);
                exp %= 100;
                result[index++] = (char) ('0' + exp / 10);
            } else if (exp >= 10) {
                result[index++] = (char) ('0' + exp / 10);
            }
            result[index++] = (char) ('0' + exp % 10);
            return index - off;
        } else {
            // Otherwise follow the Java spec for values in the interval [1E-3, 1E7).
            if (exp < 0) {
                // Decimal dot is before any of the digits.
                result[index++] = '0';
                result[index++] = '.';
                for (int i = -1; i > exp; i--) {
                    result[index++] = '0';
                }
                int current = index;
                for (int i = 0; i < olength; i++) {
                    result[current + olength - i - 1] = (char) ('0' + output % 10);
                    output /= 10;
                    index++;
                }
            } else if (exp + 1 >= olength) {
                // Decimal dot is after any of the digits.
                for (int i = 0; i < olength; i++) {
                    result[index + olength - i - 1] = (char) ('0' + output % 10);
                    output /= 10;
                }
                index += olength;
                for (int i = olength; i < exp + 1; i++) {
                    result[index++] = '0';
                }
                result[index++] = '.';
                result[index++] = '0';
            } else {
                // Decimal dot is somewhere between the digits.
                int current = index + 1;
                for (int i = 0; i < olength; i++) {
                    if (olength - i - 1 == exp) {
                        result[current + olength - i - 1] = '.';
                        current--;
                    }
                    result[current + olength - i - 1] = (char) ('0' + output % 10);
                    output /= 10;
                }
                index += olength + 1;
            }
            return index - off;
        }
    }

}
