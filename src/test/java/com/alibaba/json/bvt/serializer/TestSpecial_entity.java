package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class TestSpecial_entity extends TestCase {

    public void test_0() throws Exception {
        Assert.assertEquals("{\"name\":\"\\0\"}", JSON.toJSONString(new VO("\0")));
    }

    public void test_1() throws Exception {
        Assert.assertEquals("{\"name\":\"\\1\"}", JSON.toJSONString(new VO("\1")));
    }

    public void test_2() throws Exception {
        Assert.assertEquals("{\"name\":\"\\2\"}", JSON.toJSONString(new VO("\2")));
    }

    public void test_3() throws Exception {
        Assert.assertEquals("{\"name\":\"\\3\"}", JSON.toJSONString(new VO("\3")));
    }

    public void test_4() throws Exception {
        Assert.assertEquals("{\"name\":\"\\4\"}", JSON.toJSONString(new VO("\4")));
    }

    public void test_5() throws Exception {
        Assert.assertEquals("{\"name\":\"\\5\"}", JSON.toJSONString(new VO("\5")));
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
