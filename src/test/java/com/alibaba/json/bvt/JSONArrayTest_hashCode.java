package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class JSONArrayTest_hashCode extends TestCase {

    public void test_hashCode() throws Exception {
        Assert.assertEquals(new JSONArray().hashCode(), new JSONArray().hashCode());
    }

    public void test_hashCode_1() throws Exception {
        Assert.assertEquals(JSON.parseArray("[]"), JSON.parseArray("[]"));
    }
}
