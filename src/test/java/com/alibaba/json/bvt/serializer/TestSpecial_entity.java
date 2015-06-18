package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class TestSpecial_entity extends TestCase {

    public void test_0() throws Exception {
        Assert.assertEquals("{\"name\":\"\\u0000A\"}", JSON.toJSONString(new VO("\0A")));
    }

    public void test_1() throws Exception {
        Assert.assertEquals("{\"name\":\"\\u0001\"}", JSON.toJSONString(new VO("\1")));
    }

    public void test_2() throws Exception {
        Assert.assertEquals("{\"name\":\"\\u0002\"}", JSON.toJSONString(new VO("\2")));
    }

    public void test_3() throws Exception {
        Assert.assertEquals("{\"name\":\"\\u0003\"}", JSON.toJSONString(new VO("\3")));
    }

    public void test_4() throws Exception {
        Assert.assertEquals("{\"name\":\"\\u0004\"}", JSON.toJSONString(new VO("\4")));
    }

    public void test_5() throws Exception {
        Assert.assertEquals("{\"name\":\"\\u0005\"}", JSON.toJSONString(new VO("\5")));
    }

    public void test_6() throws Exception {
        Assert.assertEquals("{\"name\":\"\\u0006\"}", JSON.toJSONString(new VO("\6")));
    }

    public void test_7() throws Exception {
        Assert.assertEquals("{\"name\":\"\\u0007\"}", JSON.toJSONString(new VO("\7")));
    }

    public void test_8() throws Exception {
        Assert.assertEquals("{\"name\":\"\\b\"}", JSON.toJSONString(new VO("\b")));
    }

    public void test_9() throws Exception {
        Assert.assertEquals("{\"name\":\"\\t\"}", JSON.toJSONString(new VO("\t")));
    }
    
    public void test_10() throws Exception {
        Assert.assertEquals("{\"name\":\"\\n\"}", JSON.toJSONString(new VO("\n")));
    }

    public static class VO {

        private String name;

        public VO(){

        }

        public VO(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
