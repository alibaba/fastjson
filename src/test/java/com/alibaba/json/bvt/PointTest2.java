package com.alibaba.json.bvt;

import java.awt.Point;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class PointTest2 extends TestCase {

    public void test_color() throws Exception {
        Point point = new Point(3, 4);
        String text = JSON.toJSONString(point, SerializerFeature.WriteClassName);

        Point point2 = (Point) JSON.parse(text);

        Assert.assertEquals(point, point2);
    }
}
