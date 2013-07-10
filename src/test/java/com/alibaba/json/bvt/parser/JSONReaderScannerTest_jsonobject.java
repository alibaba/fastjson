package com.alibaba.json.bvt.parser;

import java.io.StringReader;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

public class JSONReaderScannerTest_jsonobject extends TestCase {

    public void test_e() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"type\\t\":'AA'}"));
        JSONObject vo = new JSONObject();
        reader.readObject(vo);
        Assert.assertEquals("AA", vo.get("type\t"));
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
