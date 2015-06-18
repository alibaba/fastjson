package com.alibaba.json.bvt.parser.deser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class DoubleArrayFieldDeserializerTest extends TestCase {

    public void test_0() throws Exception {
        Entity a = JSON.parseObject("{\"values\":[1,2]}", Entity.class);
        Assert.assertTrue(1 == a.getValues()[0]);
        Assert.assertTrue(2 == a.getValues()[1]);
    }


    public static class Entity {

        public double[] values;

        public double[] getValues() {
            return values;
        }

        public void setValues(double[] values) {
            this.values = values;
        }

    }
}
