package com.alibaba.json.bvt.bug;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Bug_for_jiangwei1 extends TestCase {

    public void test_double() throws Exception {
        JSONObject json = JSON.parseObject("{\"val\":12.3}");
        Assert.assertTrue(12.3D == json.getDoubleValue("val"));
    }

    public void test_JSONArray_double() throws Exception {
        JSONArray json = JSON.parseArray("[12.3]");
        Assert.assertTrue(12.3D == json.getDoubleValue(0));
    }
}
