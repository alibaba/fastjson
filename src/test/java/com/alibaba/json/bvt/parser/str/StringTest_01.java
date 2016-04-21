package com.alibaba.json.bvt.parser.str;

import java.util.Arrays;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import junit.framework.TestCase;

public class StringTest_01 extends TestCase {

    public void test_string() throws Exception {
        char[] chars = new char[1024];
        Arrays.fill(chars, '0');
        StringBuilder buf = new StringBuilder();
        buf.append("[\"");
        for (int i = 0; i < 16; ++i) {
            buf.append("\\\\");
            buf.append("\\\"");
            buf.append(new String(chars));
        }
        buf.append("\"]");
        
        String text = buf.toString();
        JSONArray array = (JSONArray) JSON.parse(text);
        Assert.assertEquals(1, array.size());
        
        String item = (String) array.get(0);
        Assert.assertEquals(16 * 1024 + 32, item.length());
        
        for (int i = 0; i < 16; ++i) {
            Assert.assertTrue(item.charAt(i * 1026) == '\\');
            Assert.assertTrue(item.charAt(i * 1026 + 1) == '\"');
            for (int j = 0; j < 1024; ++j) {
                Assert.assertTrue(item.charAt(i * 1026 + j + 2) == '0');    
            }
        }
    }
}
