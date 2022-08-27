package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;


public class CharTest extends TestCase {

    public void test_file() throws Exception {
        char ch = 'a';
        
        String text = JSON.toJSONString(ch);
        
        Assert.assertEquals("\"a\"", text);
        
        Character c1 = JSON.parseObject(text, Character.class);
        Character c2 = JSON.parseObject(text, char.class);
        
        Assert.assertEquals(ch, c1.charValue());
        Assert.assertEquals(ch, c2.charValue());
                
    }
}
