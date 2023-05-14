package com.alibaba.json.bvt.geo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.geo.Geometry;
import com.alibaba.fastjson.support.geo.LineString;
import com.alibaba.fastjson.support.geo.Point;
import junit.framework.TestCase;

public class PointTest
        extends TestCase {
    public void test_geo() throws Exception {
        String str = "{\n" +
                "    \"type\": \"Point\",\n" +
                "    \"coordinates\": [100.0, 0.0]\n" +
                "}";

        Geometry geometry = JSON.parseObject(str, Geometry.class);
        assertEquals(Point.class, geometry.getClass());

        assertEquals("{\"type\":\"Point\",\"coordinates\":[100.0,0.0]}", JSON.toJSONString(geometry));

        String str2 = JSON.toJSONString(geometry);
        assertEquals(str2, JSON.toJSONString(JSON.parseObject(str2, Geometry.class)));
    }
}
