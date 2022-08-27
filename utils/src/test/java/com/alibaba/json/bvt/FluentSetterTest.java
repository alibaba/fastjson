package com.alibaba.json.bvt;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class FluentSetterTest extends TestCase {

    public void test_fluent() throws Exception {
        B b = new B();
        b.setId(1001);
        b.setValue(1002);
        
        String text = JSON.toJSONString(b);
        Assert.assertEquals("{\"id\":1001,\"value\":1002}", text);
        
        B b1 = JSON.parseObject(text, B.class);
        Assert.assertEquals(b.getId(), b1.getId());
        Assert.assertEquals(b.getValue(), b1.getValue());
    }

    public static class A {

        private int id;

        public int getId() {
            return id;
        }

        public A setId(int id) {
            this.id = id;
            return this;
        }
    }

    public static class B extends A {

        private int value;

        public int getValue() {
            return value;
        }

        public B setValue(int value) {
            this.value = value;
            return this;
        }

    }
}
