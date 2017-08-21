package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class Issue1424 extends TestCase {

    public static class IntegerVal {
        private int v;

        public void setV(int v) {
            this.v = v;
        }

        @Override
        public String toString() {
            return String.valueOf(v);
        }
    }

    public static class FloatVal {
        private float v;

        public void setV(float v) {
            this.v = v;
        }

        @Override
        public String toString() {
            return String.valueOf(v);
        }
    }

    public void test_for_issue_int() {
        Map<String, Long> intOverflowMap = new HashMap<String, Long>();
        long intOverflow = Integer.MAX_VALUE;
        intOverflowMap.put("v", intOverflow + 1);
        String sIntOverflow = JSON.toJSONString(intOverflowMap);
        System.out.println("prepare to parse overflow int val: " + sIntOverflow);


        try {
            JSON.parseObject(sIntOverflow, IntegerVal.class);
        } catch (Exception e) {
            System.out.println("We captured the Exception: " + e.getMessage());
        }
    }

    public void test_for_issue_float() {
        Map<String, Double> floatOverflowMap = new HashMap<String, Double>();
        double floatOverflow = Float.MAX_VALUE;
        floatOverflowMap.put("v", floatOverflow + 1);
        String sFloatOverflow = JSON.toJSONString(floatOverflowMap);

        System.out.println(sFloatOverflow);
        assertEquals("{\"v\":3.4028234663852886E38}", sFloatOverflow);
        FloatVal floatVal = JSON.parseObject(sFloatOverflow, FloatVal.class);
        assertEquals(3.4028235E38F, floatVal.v);

        assertEquals(floatVal.v, Float.parseFloat("3.4028234663852886E38"));
    }
}
