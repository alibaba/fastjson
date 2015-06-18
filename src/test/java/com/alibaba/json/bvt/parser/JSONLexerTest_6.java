package com.alibaba.json.bvt.parser;

import java.util.LinkedList;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class JSONLexerTest_6 extends TestCase {

    public void test_scanFieldString() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"values\":[\"abc\"]}", VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class VO {

        public MyList<String> values;

        public MyList<String> getValues() {
            return values;
        }

        public void setValues(MyList<String> values) {
            this.values = values;
        }
    }

    @SuppressWarnings("serial")
    private class MyList<T> extends LinkedList<T> {

    }
}
