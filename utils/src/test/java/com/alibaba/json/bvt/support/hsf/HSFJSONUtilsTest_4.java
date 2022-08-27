package com.alibaba.json.bvt.support.hsf;

import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.alibaba.fastjson.support.hsf.MethodLocator;
import junit.framework.TestCase;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashSet;

public class HSFJSONUtilsTest_4 extends TestCase {
    public void test_for_hsf() throws Exception {
        final Method method = HSFJSONUtilsTest_4.class.getMethod("f", HashSet.class, BigDecimalDO.class);

        String json = "{\"argsTypes\":[\"java.util.HashSet\",\"com.alibaba.json.bvt.support.hsf.HSFJSONUtilsTest_4$BigDecimalDO\"],\"argsObjs\":[[{\"bd\":10.12379}],{\"$ref\":\"$.argsObjs[0][0]\"}]}";

        Object[] values = HSFJSONUtils.parseInvocationArguments(json, new MethodLocator() {

            public Method findMethod(String[] types) {
                return method;
            }
        });

        assertEquals(2, values.length);

        HashSet<BigDecimalDO> set = (HashSet<BigDecimalDO>) values[0];
        assertEquals(1, set.size());
        assertSame(set.iterator().next(), values[1]);
    }

    public static void f(HashSet<BigDecimalDO> a, BigDecimalDO b) {

    }

    public static class BigDecimalDO implements Serializable {

        /**
         * serialVersionUID
         */
        private static final long serialVersionUID = 1081203063524239676L;

        private BigDecimal bd = null;

        public BigDecimal getBd() {
            return bd;
        }

        public void setBd(BigDecimal bd) {
            this.bd = bd;
        }

    }
}
