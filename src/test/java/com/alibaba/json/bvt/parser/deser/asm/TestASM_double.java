package com.alibaba.json.bvt.parser.deser.asm;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestASM_double extends TestCase {

    public void test_asm() throws Exception {
        V0 v = new V0();
        String text = JSON.toJSONString(v);
        V0 v1 = JSON.parseObject(text, V0.class);
        
        Assert.assertTrue(v.getValue() == v1.getValue());
    }

    public static class V0 {

        private double value = 32.5F;

        public double getValue() {
            return value;
        }

        public void setValue(double i) {
            this.value = i;
        }

    }
}
