package com.alibaba.json.bvt.bug;

import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class Bug_for_lenolix_3 extends TestCase {

    public void test_0() throws Exception {

        System.out.println("{}");

        JSONObject.parseObject("{\"id\":{}}", new TypeReference<Map<String, Map<String,User>>>() {
        });
    }

    public static class User {

    }
}
