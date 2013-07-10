package com.alibaba.json.bvt.parser;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class JSONLexerTest_11 extends TestCase {

    public void test_a() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"type\":[\"AAA\"]}", VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class VO {

        public VO(){

        }

        private MyList<String> type;

        public MyList<String> getType() {
            return type;
        }

        public void setType(MyList<String> type) {
            this.type = type;
        }

    }
    
    public static class MyList<T> extends ArrayList<T> {
        public MyList() {
            throw new RuntimeException();
        }
    }
}
