package com.alibaba.json.bvt.geo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.geo.Geometry;
import com.alibaba.fastjson.support.geo.GeometryCollection;
import com.alibaba.fastjson.support.geo.LineString;
import junit.framework.TestCase;

public class GeometryCollectionTest
        extends TestCase {
    public void test_geo() throws Exception {
        String str = "{\n" +
                "    \"type\": \"GeometryCollection\",\n" +
                "    \"geometries\": [{\n" +
                "        \"type\": \"Point\",\n" +
                "        \"coordinates\": [100.0, 0.0]\n" +
                "    }, {\n" +
                "    \"type\": \"LineString\",\n" +
                "    \"coordinates\": [\n" +
                "        [101.0, 0.0],\n" +
                "        [102.0, 1.0]\n" +
                "    ]\n" +
                "    }]\n" +
                "}";

        Geometry geometry = JSON.parseObject(str, Geometry.class);
        assertEquals(GeometryCollection.class, geometry.getClass());

        assertEquals(
                "{\"type\":\"GeometryCollection\",\"geometries\":[{\"type\":\"Point\",\"coordinates\":[100.0,0.0]},{\"type\":\"LineString\",\"coordinates\":[[101.0,0.0],[102.0,1.0]]}]}"
                , JSON.toJSONString(geometry));

        String str2 = JSON.toJSONString(geometry);
        assertEquals(str2, JSON.toJSONString(JSON.parseObject(str2, Geometry.class)));
    }
}
