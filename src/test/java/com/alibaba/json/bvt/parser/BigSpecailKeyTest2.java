package com.alibaba.json.bvt.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class BigSpecailKeyTest2 extends TestCase {
    public void test_big_special_key() throws Exception {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 16; ++i) {
            char[] chars = new char[2048];
            Arrays.fill(chars, '0');
            buf.append(chars);
            
            buf.append('\\');
            buf.append('\"');
        }
        
        String key = buf.toString();
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, 1001);
        
        String text = JSON.toJSONString(map);
        JSONObject obj = JSON.parseObject(text);
        Assert.assertEquals(map.get(key), obj.get(key));
    }
    
    public void test_big_special_key_2() throws Exception {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 16; ++i) {
            char[] chars = new char[300];
            Arrays.fill(chars, '0');
            buf.append(chars);
            
            buf.append('\\');
            buf.append('\"');
        }
        
        String key = buf.toString();
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, 1001);
        
        String text = JSON.toJSONString(map);
        JSONObject obj = JSON.parseObject(text);
        Assert.assertEquals(map.get(key), obj.get(key));
    }
}
