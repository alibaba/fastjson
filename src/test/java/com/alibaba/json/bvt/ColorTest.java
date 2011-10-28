package com.alibaba.json.bvt;

import java.awt.Color;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;


public class ColorTest extends TestCase {
    public void test_color() throws Exception {
        Color color = Color.RED;
        String text = JSON.toJSONString(color);
        System.out.println(text);
        
        Color color2 = JSON.parseObject(text, Color.class);
        
        Assert.assertEquals(color, color2);
    }
}
