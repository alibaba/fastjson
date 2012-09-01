package com.alibaba.json.bvt;

import java.awt.Color;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ColorSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class ColorTest2 extends TestCase {
    public void test_color() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        Assert.assertEquals(ColorSerializer.class, serializer.getObjectWriter(Color.class).getClass());
        
        Color color = Color.RED;
        String text = JSON.toJSONString(color, SerializerFeature.WriteClassName);
        System.out.println(text);
        
        Color color2 = (Color) JSON.parse(text);
        
        Assert.assertEquals(color, color2);
    }
}
