package com.alibaba.json.bvt.geo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.geo.FeatureCollection;
import com.alibaba.fastjson.support.geo.Geometry;
import com.alibaba.fastjson.support.geo.Point;
import junit.framework.TestCase;

public class FeatureCollectionTest
        extends TestCase {
    public void test_geo() throws Exception {
        String str = "{\n" +
                "    \"type\": \"FeatureCollection\",\n" +
                "    \"features\": [{\n" +
                "       \"type\": \"Feature\",\n" +
                "       \"geometry\": {\n" +
                "           \"type\": \"Point\",\n" +
                "           \"coordinates\": [102.0, 0.5]\n" +
                "       },\n" +
                "       \"properties\": {\n" +
                "           \"prop0\": \"value0\"\n" +
                "       }\n" +
                "    }, {\n" +
                "       \"type\": \"Feature\",\n" +
                "       \"geometry\": {\n" +
                "           \"type\": \"LineString\",\n" +
                "           \"coordinates\": [\n" +
                "               [102.0, 0.0],\n" +
                "               [103.0, 1.0],\n" +
                "               [104.0, 0.0],\n" +
                "               [105.0, 1.0]\n" +
                "           ]\n" +
                "       },\n" +
                "       \"properties\": {\n" +
                "           \"prop0\": \"value0\",\n" +
                "           \"prop1\": 0.0\n" +
                "       }\n" +
                "    }, {\n" +
                "       \"type\": \"Feature\",\n" +
                "       \"geometry\": {\n" +
                "           \"type\": \"Polygon\",\n" +
                "           \"coordinates\": [\n" +
                "               [\n" +
                "                   [100.0, 0.0],\n" +
                "                   [101.0, 0.0],\n" +
                "                   [101.0, 1.0],\n" +
                "                   [100.0, 1.0],\n" +
                "                   [100.0, 0.0]\n" +
                "               ]\n" +
                "           ]\n" +
                "       },\n" +
                "       \"properties\": {\n" +
                "           \"prop0\": \"value0\",\n" +
                "           \"prop1\": {\n" +
                "               \"this\": \"that\"\n" +
                "           }\n" +
                "       }\n" +
                "    }]\n" +
                "}\n";

        Geometry geometry = JSON.parseObject(str, Geometry.class);
        assertEquals(FeatureCollection.class, geometry.getClass());

        assertEquals("{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"prop0\":\"value0\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[102.0,0.5]}},{\"type\":\"Feature\",\"properties\":{\"prop1\":\"0.0\",\"prop0\":\"value0\"},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[102.0,0.0],[103.0,1.0],[104.0,0.0],[105.0,1.0]]}},{\"type\":\"Feature\",\"properties\":{\"prop1\":\"{\\\"this\\\":\\\"that\\\"}\",\"prop0\":\"value0\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]]]}}]}", JSON.toJSONString(geometry));

        String str2 = JSON.toJSONString(geometry);
        assertEquals(str2, JSON.toJSONString(JSON.parseObject(str2, Geometry.class)));
    }
}
