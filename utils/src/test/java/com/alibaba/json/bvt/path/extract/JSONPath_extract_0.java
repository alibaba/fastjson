package com.alibaba.json.bvt.path.extract;

import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class JSONPath_extract_0 extends TestCase {
    public void test_0() throws Exception {
        String json = "{\"id\":123,\"obj\":{\"id\":123}}";

        assertEquals("{\"id\":123}"
                , JSONPath.extract(json, "$.obj")
                    .toString());
    }

    public void test_1() throws Exception {
        String json = "{\"f1\":1,\"f2\":2,\"f3\":3,\"f4\":4}";

        assertEquals("2"
                , JSONPath.extract(json, "$.f2")
                        .toString());
    }

    public void test_2() throws Exception {
        assertEquals("{}"
                , JSONPath.extract("{}", "$")
                        .toString());
    }
}
