package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class ParserSpecialCharTest extends TestCase {

    public void test_0() throws Exception {
        Assert.assertEquals("\0", JSON.parseObject("{\"value\":\"\\0\"}", VO.class).getValue());
    }
    
    public void test_1() throws Exception {
        Assert.assertEquals("\1", JSON.parseObject("{\"value\":\"\\1\"}", VO.class).getValue());
    }
    
    public void test_2() throws Exception {
        Assert.assertEquals("\2", JSON.parseObject("{\"value\":\"\\2\"}", VO.class).getValue());
    }
    
    public void test_3() throws Exception {
        Assert.assertEquals("\3", JSON.parseObject("{\"value\":\"\\3\"}", VO.class).getValue());
    }
    
    public void test_4() throws Exception {
        Assert.assertEquals("\4", JSON.parseObject("{\"value\":\"\\4\"}", VO.class).getValue());
    }
    
    public void test_5() throws Exception {
        Assert.assertEquals("\5", JSON.parseObject("{\"value\":\"\\5\"}", VO.class).getValue());
    }
    
    public void test_6() throws Exception {
        Assert.assertEquals("\6", JSON.parseObject("{\"value\":\"\\6\"}", VO.class).getValue());
    }
    
    public void test_7() throws Exception {
        Assert.assertEquals("\7", JSON.parseObject("{\"value\":\"\\7\"}", VO.class).getValue());
    }
    
    public void test_8() throws Exception {
        Assert.assertEquals("\b", JSON.parseObject("{\"value\":\"\\b\"}", VO.class).getValue());
    }
    
    public void test_9() throws Exception {
        Assert.assertEquals("\t", JSON.parseObject("{\"value\":\"\\t\"}", VO.class).getValue());
    }
    
    public void test_10() throws Exception {
        Assert.assertEquals("\n", JSON.parseObject("{\"value\":\"\\n\"}", VO.class).getValue());
    }
    
    public void test_11() throws Exception {
        Assert.assertEquals("\u000B", JSON.parseObject("{\"value\":\"\\v\"}", VO.class).getValue());
    }
    
    public void test_12() throws Exception {
        Assert.assertEquals("\f", JSON.parseObject("{\"value\":\"\\f\"}", VO.class).getValue());
    }
    
    public void test_13() throws Exception {
        Assert.assertEquals("\r", JSON.parseObject("{\"value\":\"\\r\"}", VO.class).getValue());
    }
    
    public void test_34() throws Exception {
        Assert.assertEquals("\"", JSON.parseObject("{\"value\":\"\\\"\"}", VO.class).getValue());
    }
    
    public void test_39() throws Exception {
        Assert.assertEquals("'", JSON.parseObject("{\"value\":\"\\'\"}", VO.class).getValue());
    }
    
    public void test_47() throws Exception {
        Assert.assertEquals("/", JSON.parseObject("{\"value\":\"\\/\"}", VO.class).getValue());
    }
    
    public void test_92() throws Exception {
        Assert.assertEquals("\\", JSON.parseObject("{\"value\":\"\\\\\"}", VO.class).getValue());
    }
    public static class VO {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
