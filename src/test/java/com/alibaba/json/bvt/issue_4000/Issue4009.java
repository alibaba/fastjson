package com.alibaba.json.bvt.issue_4000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.var;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.UUID;

public class Issue4009 {
    @Test
    public void test_for_issue40009_0() {
        var map = new HashMap<Object, Object>();
        UUID random = UUID.randomUUID();
        map.put(random, "foo");

        // Build JSON with feature BrowserSecure
        var json = JSON.toJSONString(map, SerializerFeature.BrowserSecure);
        String builder = "{\"" + random.toString() + "\":\"foo\"}";
        Assert.assertEquals(json, builder);
    }

    @Test
    public void test_for_issue40009_1() {
        var map = new HashMap<Object, Object>();
        UUID random = UUID.randomUUID();
        map.put(random, "foo");

        // Build JSON without feature BrowserSecure
        var json = JSON.toJSONString(map);
        String builder = "{\"" + random.toString() + "\":\"foo\"}";
        Assert.assertEquals(json, builder);
    }
}
