package com.alibaba.json.bvt.parser;

import java.util.LinkedList;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class JSONLexerTest_5 extends TestCase {

    public void test_scanFieldString() throws Exception {
        VO vo = JSON.parseObject("{\"values\":[\"abc\"]}", VO.class);
        Assert.assertEquals("abc", vo.getValues().get(0));
    }

    public static class VO {

        public LinkedList<String> values;

        public LinkedList<String> getValues() {
            return values;
        }

        public void setValues(LinkedList<String> values) {
            this.values = values;
        }
    }
}
