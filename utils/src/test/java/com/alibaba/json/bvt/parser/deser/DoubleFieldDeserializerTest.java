package com.alibaba.json.bvt.parser.deser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class DoubleFieldDeserializerTest extends TestCase {

    public void test_0() throws Exception {
        Entity a = JSON.parseObject("{\"value\":123.45}", Entity.class);
        Assert.assertTrue(123.45D == a.getValue());
    }

    public static class Entity {

        public Double value;

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

    }
}
