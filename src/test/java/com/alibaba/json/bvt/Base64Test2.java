package com.alibaba.json.bvt;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.util.Base64;

public class Base64Test2 extends TestCase {

    public void test_base64_2() throws Exception {
        new Base64();
        String text = "";
        for (int i = 0; i < 1000; ++i) {
            byte[] bytes = text.getBytes("UTF-8");
            {
                String str = com.alibaba.json.test.Base64.encodeToString(bytes, true);
                Assert.assertEquals(text, new String(Base64.decodeFast(str.toCharArray(), 0, str.length()), "UTF-8"));
                Assert.assertEquals(text, new String(Base64.decodeFast(str), "UTF-8"));
                Assert.assertEquals(text, new String(Base64.decodeFast(str, 0, str.length()), "UTF-8"));
            }
            {
                String str = com.alibaba.json.test.Base64.encodeToString(bytes, false);
                Assert.assertEquals(text, new String(Base64.decodeFast(str.toCharArray(), 0, str.length()), "UTF-8"));
                Assert.assertEquals(text, new String(Base64.decodeFast(str), "UTF-8"));
                Assert.assertEquals(text, new String(Base64.decodeFast(str, 0, str.length()), "UTF-8"));
            }
            text += ((char) i);

        }
    }
    
    public void test_illegal() throws Exception {
        String text = "abc";
        byte[] bytes = text.getBytes("UTF-8");
        String str = "\0" + com.alibaba.json.test.Base64.encodeToString(bytes, false) + "\0";
        Assert.assertEquals(text, new String(Base64.decodeFast(str.toCharArray(), 0, str.length()), "UTF-8"));
        Assert.assertEquals(text, new String(Base64.decodeFast(str), "UTF-8"));
        Assert.assertEquals(text, new String(Base64.decodeFast(str, 0, str.length()), "UTF-8"));
    }
}
