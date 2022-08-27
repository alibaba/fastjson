package com.alibaba.json.bvt.parser;

import java.io.StringReader;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;

public class JSONReaderScannerTest_error3 extends TestCase {

    public void test_e() throws Exception {
        Exception error = null;
        try {
            StringBuilder buf = new StringBuilder();
            buf.append("[{\"type\":\"");
            for (int i = 0; i < 8180; ++i) {
                buf.append('A');
            }
            buf.append("\\t");
            JSONReader reader = new JSONReader(new StringReader(buf.toString()));
            reader.readObject();
            reader.close();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class VO {

        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }
}
