package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JSONPath_16 extends TestCase {
    public void test_for_jsonpath() throws Exception {
        String str = "[{\"id\":1001,\"salary\":4000,\"name\":\"jobs\",\"valid\":false},{\"id\":1001,\"salary\":5000}]";
        System.out.println(str);
        assertEquals(2
                , JSONPath.extract(str, "$.size()"));

        assertEquals("array"
                , JSONPath.extract(str, "$.type()"));

        assertEquals("object"
                , JSONPath.extract(str, "$[0].type()"));

        assertEquals("number"
                , JSONPath.extract(str, "$[0].id.type()"));

        assertEquals("string"
                , JSONPath.extract(str, "$[0].name.type()"));

        assertEquals("boolean"
                , JSONPath.extract(str, "$[0].valid.type()"));

        assertEquals("null"
                , JSONPath.extract(str, "$[0].xx.type()"));
    }

    public void test_for_jsonpath_type() throws Exception {
        JSONObject root = new JSONObject();
        root.put("id", UUID.randomUUID());
        root.put("unit", TimeUnit.SECONDS);

        assertEquals("string"
                , JSONPath.eval(root, "$.id.type()"));

        assertEquals("string"
                , JSONPath.eval(root, "$.unit.type()"));
    }

    public void test_for_jsonpath_1() throws Exception {
        String str = "{\"id\":1001,\"salary\":4000}";
        assertNull(
                JSONPath.extract(str, "$?( @.salary > 100000 )"));

        assertEquals(JSON.parseObject(str, Feature.OrderedField),
                JSONPath.extract(str, "$?( @.salary > 1000 )"));
    }

    public void test_for_jsonpath_2() throws Exception {
        String str = "[[10,20],[100],{\"id\":1001}]";
        Object object = JSONPath.extract(str, "$.* ? (@.type() == \"array\")");
        assertEquals("[[10,20],[100]]", JSON.toJSONString(object));
    }

    public void test_for_jsonpath_3() throws Exception {
        String str = "[[10,20],[100],{\"id\":1001}]";
        Object object = JSONPath.extract(str, "$.* ? (@.type() == \"array\" && @.size() > 1)");
        assertEquals("[[10,20]]", JSON.toJSONString(object));
    }

    public void test_for_jsonpath_4() throws Exception {
        String str = "{ readings: [15.2, -22.3, 45.9] }";
        Object object = JSONPath.extract(str, "$.readings.floor()");
        assertEquals("[15,-23,45]", JSON.toJSONString(object));
    }

    public void test_for_jsonpath_5() throws Exception {
        String str = "{ readings: [15.2, 13, -22.3, 45.9] }";
        assertEquals(BigDecimal.valueOf(15), JSONPath.extract(str, "$.readings[0].floor()"));
        assertEquals(13, JSONPath.extract(str, "$.readings[1].floor()"));
    }

    public void test_for_jsonpath_6() throws Exception {
        JSONArray array = new JSONArray();
        array.add(1.1F);
        array.add(2.2D);
        array.add((byte) 3);
        array.add((short) 4);
        array.add(5);
        array.add(6L);
        array.add(BigInteger.valueOf(7));
        assertEquals(1D, JSONPath.eval(array, "$[0].floor()"));
        assertEquals(2D, JSONPath.eval(array, "$[1].floor()"));
        assertEquals((byte) 3, JSONPath.eval(array, "$[2].floor()"));
        assertEquals((short) 4, JSONPath.eval(array, "$[3].floor()"));
        assertEquals(5, JSONPath.eval(array, "$[4].floor()"));
        assertEquals(6L, JSONPath.eval(array, "$[5].floor()"));
        assertEquals(BigInteger.valueOf(7), JSONPath.eval(array, "$[6].floor()"));
    }
}
