package com.alibaba.json.demo;

import java.math.BigDecimal;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSONObject;

public class Demo1 extends TestCase {

    public void test_0() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Jobs");
        jsonObject.put("age", 50);
        jsonObject.put("salary", new BigDecimal(8000));

        String text = jsonObject.toJSONString();
        System.out.println(text);
    }
}
