package com.alibaba.json.bvt.path;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_3 extends TestCase {
    public void test_path() throws Exception {
        String a = "{\"a\":{\"b\":{\"c\":{\"d\":{\"e\":{\"f\":{\"g\":{\"h\":{\"i\":{\"j\":{\"k\":{\"l\":\"\"}}}}}}}}}}}}";
        Object x = JSON.parse(a);
        Assert.assertTrue(JSONPath.contains(x, "$.a.b.c.d.e.f.g.h.i"));
    }
}
