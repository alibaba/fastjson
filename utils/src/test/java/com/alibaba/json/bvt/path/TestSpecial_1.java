package com.alibaba.json.bvt.path;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class TestSpecial_1 extends TestCase {

    public void test_special() throws Exception {
        String x = "{\"10.0.0.1\":{\"region\":\"xxx\"}}";
        Object o = JSON.parse(x);
        Assert.assertTrue(JSONPath.contains(o, "$.10\\.0\\.0\\.1"));
        Assert.assertEquals("{\"region\":\"xxx\"}", JSONPath.eval(o, "$.10\\.0\\.0\\.1").toString());
        Assert.assertTrue(JSONPath.contains(o, "$.10\\.0\\.0\\.1.region"));
        Assert.assertEquals("xxx", JSONPath.eval(o, "$.10\\.0\\.0\\.1.region"));
    }

}
