package com.alibaba.json.demo;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSON;
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

    public void test_1() throws Exception {
        String dataString = "{\"value\":0.5D}";
        System.out.println(JSON.parse(dataString));
        dataString = "{\"value\":5D}";
        System.out.println(JSON.parse(dataString));
        dataString = "{\"value\":3e5D}";
        System.out.println(JSON.parse(dataString));
        dataString = "{\"value\":4.5F}";
        System.out.println(JSON.parse(dataString));
    }
}
