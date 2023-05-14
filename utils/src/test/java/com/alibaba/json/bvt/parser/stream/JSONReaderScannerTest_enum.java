package com.alibaba.json.bvt.parser.stream;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONReaderScanner;

public class JSONReaderScannerTest_enum extends TestCase {

    public void test_a() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser(new JSONReaderScanner("{\"type\":\"A\"}"));
        VO vo = parser.parseObject(VO.class);
        Assert.assertEquals(Type.A, vo.getType());
        parser.close();
    }

    public void test_b() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser(new JSONReaderScanner("{\"type\":\"B\"}"));
        VO vo = parser.parseObject(VO.class);
        Assert.assertEquals(Type.B, vo.getType());
        parser.close();
    }

    public void test_c() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser(new JSONReaderScanner("{\"type\":\"C\"}"));
        VO vo = parser.parseObject(VO.class);
        Assert.assertEquals(Type.C, vo.getType());
        parser.close();
    }
    
    public void test_x() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser(new JSONReaderScanner("{\"type\":\"XXXXXXXXXXXXXXXXXXXXXXXX\"}"));
        VO vo = parser.parseObject(VO.class);
        Assert.assertEquals(Type.XXXXXXXXXXXXXXXXXXXXXXXX, vo.getType());
        parser.close();
    }

    public static class VO {

        private Type type;

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

    }

    public static enum Type {
        A, B, C, D, XXXXXXXXXXXXXXXXXXXXXXXX
    }
}
