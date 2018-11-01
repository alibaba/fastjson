package com.alibaba.json.bvt.util;

import com.alibaba.fastjson.util.RyuFloat;
import junit.framework.TestCase;

import java.util.Random;

public class RyuFloatTest extends TestCase {
    public void test_for_ryu() throws Exception {
        Random random = new Random();

        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            float value = random.nextFloat();

            String str1 = Float.toString(value);
            String str2 = RyuFloat.toString(value);

            if (!str1.equals(str2)) {
                boolean cmp = (Float.parseFloat(str1) == Float.parseFloat(str2));
                System.out.println(str1 + " -> " + str2 + " : " + cmp);
                assertTrue(cmp);
//                assertTrue(Float.parseFloat(str1) == Float.parseFloat(str2));
            }
        }
    }

    public void test_0() throws Exception {
        float[] values = new float[] {
                Float.NaN,
                Float.NEGATIVE_INFINITY,
                Float.POSITIVE_INFINITY,
                Float.MIN_VALUE,
                Float.MAX_VALUE,
                0,
                0.0f,
                -0.0f,
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                Long.MAX_VALUE,
                Long.MIN_VALUE,
                Float.intBitsToFloat(0x80000000),
                1.0f,
                -1f,
                Float.intBitsToFloat(0x00800000),
                1.0E7f,
                9999999.0f,
                0.001f,
                0.0009999999f,
                Float.intBitsToFloat(0x7f7fffff),
                Float.intBitsToFloat(0x00000001),
                3.3554448E7f,
                8.999999E9f,
                3.4366717E10f,
                0.33007812f,
                Float.intBitsToFloat(0x5D1502F9),
                Float.intBitsToFloat(0x5D9502F9),
                Float.intBitsToFloat(0x5E1502F9),
                4.7223665E21f,
                8388608.0f,
                1.6777216E7f,
                3.3554436E7f,
                6.7131496E7f,
                1.9310392E-38f,
                -2.47E-43f,
                1.993244E-38f,
                4103.9003f,
                5.3399997E9f,
                6.0898E-39f,
                0.0010310042f,
                2.8823261E17f,
                7.038531E-26f,
                9.2234038E17f,
                6.7108872E7f,
                1.0E-44f,
                2.816025E14f,
                9.223372E18f,
                1.5846085E29f,
                1.1811161E19f,
                5.368709E18f,
                4.6143165E18f,
                0.007812537f,
                1.4E-45f,
                1.18697724E20f,
                1.00014165E-36f,
                200f,
                3.3554432E7f,

                0.1f,
                0.01f,
                0.001f,
                0.0001f,
                0.00001f,
                0.000001f,
                0.0000001f,

                1.1f,
                1.01f,
                1.001f,
                1.0001f,
                1.00001f,
                1.000001f,
                1.0000001f,
        };

        for (float value : values) {
            String str1 = Float.toString(value);
            String str2 = RyuFloat.toString(value);

            if (!str1.equals(str2)) {
                boolean cmp = (Float.parseFloat(str1) == Float.parseFloat(str2));
                System.out.println(str1 + " -> " + str2 + " : " + cmp);
                assertTrue(cmp);
            }
        }
    }
}
