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
                Float.NaN, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, Float.MIN_VALUE, Float.MAX_VALUE, 0, 0.0f, -0.0f
                , Integer.MAX_VALUE, Integer.MIN_VALUE
                , Long.MAX_VALUE, Long.MIN_VALUE
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
