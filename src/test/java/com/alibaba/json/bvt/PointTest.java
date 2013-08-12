package com.alibaba.json.bvt;

import java.awt.Point;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PointCodec;

public class PointTest extends TestCase {

    public void test_color() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        Assert.assertEquals(PointCodec.class, serializer.getObjectWriter(Point.class).getClass());
        
        Point point = new Point(3, 4);
        String text = JSON.toJSONString(point);

        Point point2 = JSON.parseObject(text, Point.class);

        Assert.assertEquals(point, point2);
    }
}
