package com.alibaba.json.bvt.path.odps_udf;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.support.odps.udf.JSONContainsValue;


public class JSONContainsValue_Test extends TestCase {
    public void test_contians() throws Exception {
        JSONContainsValue udf = new JSONContainsValue();
        Assert.assertTrue(udf.evaluate("{\"name\":\"123\"}", "$.name", "123"));
        Assert.assertFalse(udf.evaluate("{\"name\":\"123\"}", "$.name", "124"));
        Assert.assertTrue(udf.evaluate("{\"name\":\"123\"}", "$.value", (Long) null));
    }
    
    public void test_array_contians() throws Exception {
        JSONContainsValue udf = new JSONContainsValue();
        Assert.assertTrue(udf.evaluate("{\"name\":[\"123\"]}", "$.name", "123"));
        Assert.assertFalse(udf.evaluate("{\"name\":[\"123\"]}", "$.name", "124"));
        Assert.assertTrue(udf.evaluate("{\"name\":[\"123\"]}", "$.value", (Long) null));
        Assert.assertFalse(udf.evaluate("{\"name\":[\"123\"]}", "$.name", (Long) null));
    }
}
