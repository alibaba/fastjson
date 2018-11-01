package com.alibaba.json.bvt.path.extract;

import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

import java.net.InetSocketAddress;

public class JSONPath_extract_1 extends TestCase {
    public void test_0() throws Exception {
        String json = "[{\"id\":1001},{\"id\":1002},{\"id\":1003},[1],123,-4,\"a\\\"bc\"]";

        assertEquals("{\"id\":1001}"
                , JSONPath.extract(json, "$.0")
                    .toString());

        assertEquals("{\"id\":1002}"
                , JSONPath.extract(json, "$.1")
                        .toString());


        assertEquals("{\"id\":1003}"
                , JSONPath.extract(json, "$.2")
                        .toString());

        assertEquals("[1]"
                , JSONPath.extract(json, "$.3")
                        .toString());

        assertEquals("123"
                , JSONPath.extract(json, "$.4")
                        .toString());

        assertEquals("-4"
                , JSONPath.extract(json, "$.5")
                        .toString());

        assertEquals("a\"bc"
                , JSONPath.extract(json, "$.6")
                        .toString());
    }

    public void test_1() throws Exception {
        String json = "[\"a\\\"bc\",123]";

        assertEquals("a\"bc"
                , JSONPath.extract(json, "$.0")
                        .toString());

        assertEquals("123"
                , JSONPath.extract(json, "$.1")
                        .toString());
    }

    public void test_2() throws Exception {
        String json = "[\"a\\\\bc\",123]";

        assertEquals("a\\bc"
                , JSONPath.extract(json, "$.0")
                        .toString());

        assertEquals("123"
                , JSONPath.extract(json, "$.1")
                        .toString());
    }

    public void test_3() throws Exception {
        String json = "[\"a\\\"b\\\\c\\\"d\\\"e\",123]";

        assertEquals("a\"b\\c\"d\"e"
                , JSONPath.extract(json, "$.0")
                        .toString());

        assertEquals("123"
                , JSONPath.extract(json, "$.1")
                        .toString());

        assertNull(JSONPath.extract(json, "$.2"));
    }
}
