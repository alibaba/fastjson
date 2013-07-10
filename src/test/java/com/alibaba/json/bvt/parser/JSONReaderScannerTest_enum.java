package com.alibaba.json.bvt.parser;

import java.io.StringReader;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;

public class JSONReaderScannerTest_enum extends TestCase {

    public void test_e() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{type:'AA'}"));
        VO vo2 = reader.readObject(VO.class);
        Assert.assertEquals(Type.AA, vo2.getType());
        reader.close();
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
        AA, BB, CC
    }
}
