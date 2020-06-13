package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

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
}
