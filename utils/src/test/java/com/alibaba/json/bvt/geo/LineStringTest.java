package com.alibaba.json.bvt.geo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.geo.Geometry;
import com.alibaba.fastjson.support.geo.LineString;
import junit.framework.TestCase;

public class LineStringTest extends TestCase {
    public void test_geo() throws Exception {
        String str = "{\n" +
                "    \"type\": \"LineString\",\n" +
                "    \"coordinates\": [\n" +
                "        [100.0, 0.0],\n" +
                "        [101.0, 1.0]\n" +
                "    ]\n" +
                "}";

        Geometry geometry = JSON.parseObject(str, Geometry.class);
        assertEquals(LineString.class, geometry.getClass());

        assertEquals("{\"type\":\"LineString\",\"coordinates\":[[100.0,0.0],[101.0,1.0]]}", JSON.toJSONString(geometry));

        String str2 = JSON.toJSONString(geometry);
        assertEquals(str2, JSON.toJSONString(JSON.parseObject(str2, Geometry.class)));
    }
}
