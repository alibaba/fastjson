package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_lenolix_1 extends TestCase {

    public void test_0() throws Exception {
        Map<String, User> matcherMap = new HashMap<String, User>();
        String matcherMapString = JSON.toJSONString(matcherMap, SerializerFeature.WriteClassName,
                                                    SerializerFeature.WriteMapNullValue);
        
        System.out.println(matcherMapString);
        
        matcherMap = JSONObject.parseObject(matcherMapString, new TypeReference<Map<String, User>>() {
        });
    }

    public static class User {

    }
}
