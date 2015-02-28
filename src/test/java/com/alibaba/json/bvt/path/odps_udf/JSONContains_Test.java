package com.alibaba.json.bvt.path.odps_udf;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.support.odps.udf.JSONContains;

public class JSONContains_Test extends TestCase {

    public void test_contians() throws Exception {
        JSONContains udf = new JSONContains();
        Assert.assertTrue(udf.evaluate("{\"name\":\"123\"}", "$.name"));
        Assert.assertFalse(udf.evaluate("{\"name\":\"123\"}", "$.value"));
    }

}
