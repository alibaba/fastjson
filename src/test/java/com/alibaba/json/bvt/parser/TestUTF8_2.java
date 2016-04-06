package com.alibaba.json.bvt.parser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;

import junit.framework.TestCase;

public class TestUTF8_2 extends TestCase {

    public void test_utf_1() throws Exception {
        String content = new String(decodeHex("F0A4ADA2".toCharArray()), "UTF-8");
        JSONObject json = new JSONObject();
        json.put("content", content);
        JSONObject obj = (JSONObject) JSON.parse(json.toJSONString().getBytes("UTF-8"));
        Assert.assertEquals(1, obj.size());
        Assert.assertEquals(content, obj.get("content"));
    }

    public void test_utf_2() throws Exception {
        String content = new String(decodeHex("E282AC".toCharArray()), "UTF-8");
        JSONObject json = new JSONObject();
        json.put("content", content);
        JSONObject obj = (JSONObject) JSON.parse(json.toJSONString().getBytes("UTF-8"));
        Assert.assertEquals(1, obj.size());
        Assert.assertEquals(content, obj.get("content"));
    }

    public void test_utf_3() throws Exception {
        byte[] bytes = decodeHex("C2A2".toCharArray());
        String content = new String(bytes, "UTF-8");
        JSONObject json = new JSONObject();
        json.put("content", content);
        JSONObject obj = (JSONObject) JSON.parse(json.toJSONString().getBytes("UTF-8"));
        Assert.assertEquals(1, obj.size());
        Assert.assertEquals(content, obj.get("content"));
    }
    
    public void test_utf_4() throws Exception {
        IOUtils.clearChars();
        
        byte[] bytes = decodeHex("C2FF".toCharArray());
        String content = new String(bytes, "UTF-8");
        JSONObject json = new JSONObject();
        json.put("content", content);
        JSONObject obj = (JSONObject) JSON.parse(json.toJSONString().getBytes("UTF-8"));
        Assert.assertEquals(1, obj.size());
        Assert.assertEquals(content, obj.get("content"));
    }
    
    public static byte[] decodeHex(char[] data) throws Exception {

        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new Exception("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }
    
    protected static int toDigit(char ch, int index) throws Exception {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new Exception("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }
}
