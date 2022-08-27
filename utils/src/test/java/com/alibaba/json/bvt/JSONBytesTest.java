package com.alibaba.json.bvt;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class JSONBytesTest extends TestCase {

    public void test_codec() throws Exception {
        int len = (Character.MAX_VALUE - Character.MIN_VALUE) + 1;
        char[] chars = new char[len];
        for (int i = 0; i < len; ++i) {
            char ch = (char) ((int) Character.MAX_VALUE + i);
            if (ch >= 55296 && ch <= 57344) {
                continue;
            }
            chars[i] = ch;
        }
        
        String text = new String(chars);
        
        byte[] bytes = JSON.toJSONBytes(text);
        String text2 = (String) JSON.parse(bytes);
        
        Assert.assertEquals(text.length(), text2.length());
        for (int i = 0; i < len; ++i) {
            char c1 = text.charAt(i);
            char c2 = text2.charAt(i);
            
            Assert.assertEquals(c1, c2);
        }
    }
    
    
}
