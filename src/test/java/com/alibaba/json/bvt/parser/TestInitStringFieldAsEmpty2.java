package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

public class TestInitStringFieldAsEmpty2 extends TestCase {

    public void test_public() throws Exception {
        VO1 vo1 = JSON.parseObject("{\"id\":0,\"value\":33, \"o\":{}}", VO1.class, Feature.InitStringFieldAsEmpty);
        Assert.assertEquals("", vo1.getName());
        Assert.assertEquals("", vo1.getO().getValue());
    }

    public static class VO1 {

        private int    id;

        private String name;

        private int    value;

        private VO2    o;

        public VO1(){

        }

        public VO2 getO() {
            return o;
        }

        public void setO(VO2 o) {
            this.o = o;
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

    public static class VO2 {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
