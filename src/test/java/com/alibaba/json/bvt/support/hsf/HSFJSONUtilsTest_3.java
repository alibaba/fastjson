package com.alibaba.json.bvt.support.hsf;

import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.alibaba.fastjson.support.hsf.MethodLocator;
import junit.framework.TestCase;

import java.lang.reflect.Method;

public class HSFJSONUtilsTest_3 extends TestCase {
    public void test_for_hsf() throws Exception {
        final Method method = HSFJSONUtilsTest_3.class.getMethod("f", int.class, long.class);

        String json = "[[1,2]]";

        Object[] values = HSFJSONUtils.parseInvocationArguments(json, new MethodLocator() {

            public Method findMethod(String[] types) {
                return method;
            }
        });

        assertEquals(2, values.length);
        assertEquals(1, values[0]);
        assertEquals(2L, values[1]);
    }

    public void test_for_hsf_1() throws Exception {
        final Method method = HSFJSONUtilsTest_3.class.getMethod("f", int.class, long.class);

        String json = "[\n" +
                "[1, 2]\n" +
                "]";

        Object[] values = HSFJSONUtils.parseInvocationArguments(json, new MethodLocator() {

            public Method findMethod(String[] types) {
                return method;
            }
        });

        assertEquals(2, values.length);
        assertEquals(1, values[0]);
        assertEquals(2L, values[1]);
    }

    public void test_for_hsf_2() throws Exception {
        final Method method = HSFJSONUtilsTest_3.class.getMethod("f", int.class, long.class);

        String json = "[[\"1\",\"2\"]]";

        Object[] values = HSFJSONUtils.parseInvocationArguments(json, new MethodLocator() {

            public Method findMethod(String[] types) {
                return method;
            }
        });

        assertEquals(2, values.length);
        assertEquals(1, values[0]);
        assertEquals(2L, values[1]);
    }

    public static void f(int a, long b) {

    }
}
