package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Issue125 extends TestCase {

    public void test_for_issue() throws Exception {
        String content = "{\"data\":\"sfasfasdfasdfas\\r" + String.valueOf((char) 160) + "\\rasdfasdfasd\"}";
        JSONObject jsonObject = JSON.parseObject(content);
        System.out.println(jsonObject);
    }
}
