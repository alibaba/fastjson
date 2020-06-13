package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

public class JSONPath_16 extends TestCase {
    public void test_for_jsonpath() throws Exception {
        String str = "[{\"id\":1001,\"salary\":4000},{\"id\":1001,\"salary\":5000}]";
        assertEquals(2
                , JSONPath.extract(str, "$.size()"));
    }

    public void test_for_jsonpath_1() throws Exception {
        String str = "{\"id\":1001,\"salary\":4000}";
        assertNull(
                JSONPath.extract(str, "$?( @.salary > 100000 )"));

        assertEquals(JSON.parseObject(str, Feature.OrderedField),
                JSONPath.extract(str, "$?( @.salary > 1000 )"));
    }
}
