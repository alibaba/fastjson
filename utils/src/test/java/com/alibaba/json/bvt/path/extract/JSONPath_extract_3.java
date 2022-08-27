package com.alibaba.json.bvt.path.extract;

import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class JSONPath_extract_3 extends TestCase {

    public void test_0() throws Exception {
        assertEquals("male"
                , JSONPath.extract(json, "$[0]['gender']")
                    .toString());
    }

    public void test_1() throws Exception {
        assertNull(JSONPath.extract(json, "$[1]['gender']"));
    }

    public void test_2() throws Exception {
        assertEquals("ben"
                , JSONPath.extract(json, "$[1]['name']").toString());
    }

    private static final String json = "[\n" +
            "   {\n" +
            "      \"name\" : \"john\",\n" +
            "      \"gender\" : \"male\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"name\" : \"ben\"\n" +
            "   }\n" +
            "]";
}
