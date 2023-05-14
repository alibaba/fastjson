package com.alibaba.json.bvt.bug;

import java.util.Collections;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class Issue689 extends TestCase {
    public void test_0() throws Exception {
        Map<String, ?> map = Collections.singletonMap("value", "A B");
        String json = JSON.toJSONString(map);
        Assert.assertEquals("{\"value\":\"A B\"}", json);
        
        JSONObject obj = JSON.parseObject(json);
        Assert.assertEquals(obj.get("value"), map.get("value"));
    }
}
