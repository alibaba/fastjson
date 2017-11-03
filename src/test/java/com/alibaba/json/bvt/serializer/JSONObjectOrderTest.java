package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class JSONObjectOrderTest extends TestCase {
    public void test_for_order() throws Exception {
        System.out.println(JSON.VERSION);
        JSONObject jsonObj = new JSONObject(true);
        jsonObj.put("code","code");
        jsonObj.put("msg","msg");
        jsonObj.put("data", "data");
        String jsonStr = JSON.toJSONString(jsonObj, SerializerFeature.MapSortField);
        assertEquals("{\"code\":\"code\",\"msg\":\"msg\",\"data\":\"data\"}", jsonStr);
    }
}
