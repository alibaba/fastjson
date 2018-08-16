package com.alibaba.json.bvt.util;

import com.alibaba.fastjson.util.RyuDouble;
import junit.framework.TestCase;

import java.util.Random;

public class RyuDoubleTest extends TestCase {
    public void test_for_ryu() throws Exception {
        Random random = new Random();

        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            double value = random.nextDouble();

            String str1 = Double.toString(value);
            String str2 = RyuDouble.toString(value);

            if (!str1.equals(str2)) {
                System.out.println(str1 + " -> " + str2);
                assertTrue(Double.parseDouble(str1) == Double.parseDouble(str2));
            }
        }
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

                1.797693134862315E308,
                1.79769313486231E308,
                1.7976931348623E308,
                1.797693134862E308,
                1.79769313486E308,
                1.7976931348E308,
                1.797693134E308,
                1.79769313E308,
                1.7976931E308,
                1.797693E308,
                1.79769E308,
                1.7976E308,
                1.797E308,
                1.79E308,
                1.7E308,
                1E308,

                1.797693134862315,
                1.79769313486231,
                1.7976931348623,
                1.797693134862,
                1.79769313486,
                1.7976931348,
                1.797693134,
                1.79769313,
                1.7976931,
                1.797693,
                1.79769,
                1.7976,
                1.797,
                1.79,
                1.7,
                1,

                -1.797693134862315,
                -1.79769313486231,
                -1.7976931348623,
                -1.797693134862,
                -1.79769313486,
                -1.7976931348,
                -1.797693134,
                -1.79769313,
                -1.7976931,
                -1.797693,
                -1.79769,
                -1.7976,
                -1.797,
                -1.79,
                -1.7,
                -1,

                0.1,
                0.01,
                0.001,
                0.0001,
                0.00001,
                0.000001,
                0.0000001,
                0.00000001,
                0.000000001,
                0.0000000001,
                0.00000000001,
                0.000000000001,
                0.0000000000001,
                0.00000000000001,

                -0.1,
                -0.01,
                -0.001,
                -0.0001,
                -0.00001,
                -0.000001,
                -0.0000001,
                -0.00000001,
                -0.000000001,
                -0.0000000001,
                -0.00000000001,
                -0.000000000001,
                -0.0000000000001,
                -0.00000000000001,

                1.1E1,
                1.1E2,
                1.1E3,
                1.1E4,
                1.1E5,
                1.1E6,
                1.1E7,
                1.1E8,
                1.1E9,
                1.1E10,

                -1.1E1,
                -1.1E2,
                -1.1E3,
                -1.1E4,
                -1.1E5,
                -1.1E6,
                -1.1E7,
                -1.1E8,
                -1.1E9,
                -1.1E10,

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
