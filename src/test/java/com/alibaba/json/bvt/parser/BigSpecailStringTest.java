package com.alibaba.json.bvt.parser;

import java.util.Arrays;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class BigSpecailStringTest extends TestCase {
    public void test_big_special_key() throws Exception {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 16; ++i) {
            buf.append('\\');
            buf.append('\"');
            char[] chars = new char[1024];
            Arrays.fill(chars, '0');
            buf.append(chars);
        }
        
        String text = buf.toString();
        
        String json = JSON.toJSONString(text);
        
        String text2 = (String) JSON.parse(json);
        Assert.assertEquals(text, text2);
    }
}
