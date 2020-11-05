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

public class JSONPath_17 extends TestCase {
    public void test_for_jsonpath() throws Exception {
        String input = "{\"b\":[{\"c\":{\"d\":{\"e\":\"978\"}},\"f\":{\"c\":{\"d\":{\"$ref\":\"$.b[0].c.d\"}}}}]}";
        Object obj = JSON.parse(input);
        String oupput = JSON.parse(input).toString();
        assertEquals(obj, JSON.parse(oupput));
    }

    public void test_for_jsonpath_1() throws Exception {
        assertEquals("[5]", JSONPath.extract("[1, 2, 3, 4, 5]", "$[last]").toString());
    }
}
