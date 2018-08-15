package com.alibaba.json.test.ryu;

import com.alibaba.fastjson.util.RyuDouble;
import junit.framework.TestCase;

import java.util.Random;

public class RyuDoubleTest extends TestCase {
    public void test_for_ryu() throws Exception {
        Random random = new Random();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 100; ++i) {
            double value = random.nextDouble();

            String str1 = Double.toString(value);
            String str2 = RyuDouble.toString(value);

            if (!str1.equals(str2)) {
                System.out.println(str1 + " -> " + str2);
                assertTrue(Double.parseDouble(str1) == Double.parseDouble(str2));
            }
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public void test_0() throws Exception {
        double[] values = new double[] {
                Double.NaN
                , Double.NEGATIVE_INFINITY
                , Double.POSITIVE_INFINITY
                , Double.MIN_VALUE
                , Double.MAX_VALUE
                , 0, 0.0d, -0.0d
                , Long.MAX_VALUE
                , Long.MIN_VALUE
                , Integer.MAX_VALUE
                , Integer.MIN_VALUE
        };

        for (double value : values) {
            String str1 = Double.toString(value);
            String str2 = RyuDouble.toString(value);

            assertEquals(str1, str2);
        }
    }
}
