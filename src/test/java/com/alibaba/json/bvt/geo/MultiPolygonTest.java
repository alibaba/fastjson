package com.alibaba.json.bvt.geo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.geo.Geometry;
import com.alibaba.fastjson.support.geo.MultiPoint;
import com.alibaba.fastjson.support.geo.MultiPolygon;
import junit.framework.TestCase;

public class MultiPolygonTest
        extends TestCase {
    public void test_geo() throws Exception {
        String str = "{\n" +
                "    \"type\": \"MultiPolygon\",\n" +
                "    \"coordinates\": [\n" +
                "        [\n" +
                "            [\n" +
                "                [102.0, 2.0],\n" +
                "                [103.0, 2.0],\n" +
                "                [103.0, 3.0],\n" +
                "                [102.0, 3.0],\n" +
                "                [102.0, 2.0]\n" +
                "            ]\n" +
                "        ],\n" +
                "        [\n" +
                "            [\n" +
                "                [100.0, 0.0],\n" +
                "                [101.0, 0.0],\n" +
                "                [101.0, 1.0],\n" +
                "                [100.0, 1.0],\n" +
                "                [100.0, 0.0]\n" +
                "            ],\n" +
                "            [\n" +
                "                [100.2, 0.2],\n" +
                "                [100.2, 0.8],\n" +
                "                [100.8, 0.8],\n" +
                "                [100.8, 0.2],\n" +
                "                [100.2, 0.2]\n" +
                "            ]\n" +
                "        ]\n" +
                "    ]\n" +
                "}";

        Geometry geometry = JSON.parseObject(str, Geometry.class);
        assertEquals(MultiPolygon.class, geometry.getClass());

        assertEquals(
                "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[102.0,2.0],[103.0,2.0],[103.0,3.0],[102.0,3.0],[102.0,2.0]]],[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],[[100.2,0.2],[100.2,0.8],[100.8,0.8],[100.8,0.2],[100.2,0.2]]]]}"
                , JSON.toJSONString(geometry));

        String str2 = JSON.toJSONString(geometry);
        assertEquals(str2, JSON.toJSONString(JSON.parseObject(str2, Geometry.class)));
    }
}
