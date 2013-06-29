package com.alibaba.json.bvt.parser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class JSONLexerTest_4 extends TestCase {

    public void test_scanFieldString() throws Exception {
        VO vo = JSON.parseObject("{\"value\":\"abc\"}", VO.class);
        Assert.assertEquals("abc", vo.getValue());
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
