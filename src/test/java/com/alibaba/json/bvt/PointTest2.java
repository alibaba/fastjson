package com.alibaba.json.bvt;

import java.awt.Point;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.AwtCodec;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class PointTest2 extends TestCase {

    public void test_point() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        Assert.assertEquals(AwtCodec.class, serializer.getObjectWriter(Point.class).getClass());
        
        Point point = new Point(3, 4);
        String text = JSON.toJSONString(point, SerializerFeature.WriteClassName);

        Object obj = JSON.parse(text);
        Point point2 = (Point) obj;

        Assert.assertEquals(point, point2);

        Point point3 = (Point) JSON.parseObject(text, Point.class);

        Assert.assertEquals(point, point3);
    }

    public void test_point2() throws Exception {
        JSON.parseObject("{}", Point.class);
        JSON.parseArray("[null,null]", Point.class);
        Assert.assertNull(JSON.parseObject("null", Point.class));
        JSON.parseObject("{\"@type\":\"java.awt.Point\"}", Point.class);
        JSON.parseObject("{\"value\":null}", VO.class);
    }

    public static class VO {

        private Point value;

        public Point getValue() {
            return value;
        }

        public void setValue(Point value) {
            this.value = value;
        }

    }
}
