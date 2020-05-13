package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class JSONScannerTest__x extends TestCase {

    public void test_x() throws Exception {
        StringBuilder buf = new StringBuilder();
        buf.append("\"");
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                buf.append("\\x");
                buf.append(Integer.toHexString(i));
                buf.append(Integer.toHexString(j));
            }
        }
        buf.append("\"");
        String jsonString = (String) JSON.parse(buf.toString());
        Assert.assertEquals(16 * 16, jsonString.length());
        for (int i = 0; i < 16 * 16; ++i) {
            char c = jsonString.charAt(i);
            if ((int) c != i) {
                Assert.fail();
            }
        }
    }
}
