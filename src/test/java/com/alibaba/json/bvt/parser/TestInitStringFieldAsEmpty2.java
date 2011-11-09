package com.alibaba.json.bvt.parser;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

public class TestInitStringFieldAsEmpty2 extends TestCase {

    public void test_public() throws Exception {
        VO1 vo1 = JSON.parseObject("{\"id\":0,\"value\":33}", VO1.class, Feature.InitStringFieldAsEmpty);
        Assert.assertEquals("", vo1.getName());
    }

    public static class VO1 {

        private int    id;

        private String name;

        private int    value;

        public VO1() {

        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
