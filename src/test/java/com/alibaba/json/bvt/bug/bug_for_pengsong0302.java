package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class bug_for_pengsong0302 extends TestCase {

    public void test_0() throws Exception {
        Assert.assertEquals("\"a\\u2028b\"", JSON.toJSONString("a\u2028b"));
    }
    
    public void test_1() throws Exception {
        Assert.assertEquals("{\"value\":\"a\\u2028b\"}", JSON.toJSONString(new A("a\u2028b")));
    }

    public static class A {

        private String value;

        public A(String value){
            super();
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
