package com.alibaba.json.bvt.path;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_4 extends TestCase {

    public void test_path() throws Exception {
        String a = "{\"key\":\"value\",\"10.0.1.1\":\"haha\"}";
        Object x = JSON.parse(a);
        JSONPath.set(x, "$.test", "abc");
        Object o = JSONPath.eval(x, "$.10\\.0\\.1\\.1");
        Assert.assertEquals("haha", o);
    }
}
