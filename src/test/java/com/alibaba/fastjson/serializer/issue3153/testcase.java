package com.alibaba.fastjson.serializer.issue3153;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class testcase {

    @Test
    public void testIssue3153() {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("a.b", "str");
        
        String jsonstr1 = JSON.toJSONString(itemMap);
        Assert.assertEquals("{\"a.b\":\"str\"}", jsonstr1);
        JSONObject target1 = JSON.parseObject(jsonstr1);
        Assert.assertEquals(itemMap, target1);

        String jsonstr2 = JSON.toJSONString(itemMap, SerializerFeature.WRITE_MAP_NULL_FEATURES);
        Assert.assertEquals("{\"a.b\":\"str\"}", jsonstr2);
        JSONObject target2 = JSON.parseObject(jsonstr2);
        Assert.assertEquals(itemMap, target2);
    }

}
