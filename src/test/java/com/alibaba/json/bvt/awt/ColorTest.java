package com.alibaba.json.bvt.awt;

import java.awt.Color;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.AwtCodec;
import com.alibaba.fastjson.serializer.JSONSerializer;

import junit.framework.TestCase;


public class ColorTest extends TestCase {
    public void test_color() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        Assert.assertEquals(AwtCodec.class, serializer.getObjectWriter(Color.class).getClass());
        
        Color color = Color.RED;
        String text = JSON.toJSONString(color);
        System.out.println(text);
        
        Color color2 = JSON.parseObject(text, Color.class);
        
        Assert.assertEquals(color, color2);
    }
}
