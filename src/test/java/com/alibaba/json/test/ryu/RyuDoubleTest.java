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
                Double.NaN,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                Double.MIN_VALUE,
                Double.MAX_VALUE,

                0,
                0.0d,
                -0.0d,
                Double.longBitsToDouble(0x8000000000000000L),
                Double.NaN,

                Long.MAX_VALUE,
                Long.MIN_VALUE,
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                Double.longBitsToDouble(0x0010000000000000L),

                9999999.999999998d,
                0.0009999999999999998d,
                1.0E7d,
                0.001d,
                Double.longBitsToDouble(0x7fefffffffffffffL),

                Double.longBitsToDouble(1),
                -2.109808898695963E16,
                4.940656E-318d,
                1.18575755E-316d,
                2.989102097996E-312d,
                9.0608011534336E15d,
                4.708356024711512E18,
                9.409340012568248E18,
                1.8531501765868567E21,
                -3.347727380279489E33,
                1.9430376160308388E16,
                -6.9741824662760956E19,
                4.3816050601147837E18,
        };

        for (int i = 0; i < values.length; i++) {
            double value = values[i];
            String str1 = Double.toString(value);
            String str2 = RyuDouble.toString(value);

            if (!str1.equals(str2)) {
                boolean cmp = (Double.parseDouble(str1) == Double.parseDouble(str2));
                System.out.println(str1 + " -> " + str2 + " : " + cmp);
                assertTrue(cmp);
            }
        }
    }
}
