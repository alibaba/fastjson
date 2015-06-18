package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestDouble extends TestCase {

    public void test_doubleArray_2() throws Exception {
        double[] array = new double[] { 1, 2 };
        A a = new A();
        a.setValue(array);

        String text = JSON.toJSONString(a);
        A a1 = JSON.parseObject(text, A.class);
    }

    public static class A {

        private double[] value;

        public double[] getValue() {
            return value;
        }

        public void setValue(double[] value) {
            this.value = value;
        }
    }
}
