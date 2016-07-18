package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class DoubleTest_1 extends TestCase {

    public void test_double() throws Exception {
        Model model = new Model();
        model.value = 0.00000001D;
        String text = JSON.toJSONString(model);
        System.out.println(text);
    }

    public static class Model {

        private double value;

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

    }
}
