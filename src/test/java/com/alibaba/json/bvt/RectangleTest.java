package com.alibaba.json.bvt;

import java.awt.Rectangle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RectangleTest extends TestCase {

    public void test_color() throws Exception {
        Rectangle v = new Rectangle(3, 4, 100, 200);
        String text = JSON.toJSONString(v, SerializerFeature.WriteClassName);
        
        System.out.println(text);

        Rectangle v2 = (Rectangle) JSON.parse(text);

        Assert.assertEquals(v, v2);
    }
}
