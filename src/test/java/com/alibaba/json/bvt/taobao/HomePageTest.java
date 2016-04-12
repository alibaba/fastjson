package com.alibaba.json.bvt.taobao;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.json.test.benchmark.decode.TradeObjectParse;

import junit.framework.TestCase;

public class HomePageTest extends TestCase {
    private String text;
    
    protected void setUp() throws Exception {
        InputStream is = TradeObjectParse.class.getClassLoader().getResourceAsStream("json/taobao/homepage_1.json");
        text = IOUtils.toString(is);
        is.close();
    }
    
    public void test_decode() throws Exception {
        JSON.parseObject(text, GetHomePageResponse.class);
    }

    public void test_parse() throws Exception {
        JSONObject object = JSON.parseObject(text);
        System.out.println(JSON.toJSONString(object));
    }
}
