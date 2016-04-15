package com.alibaba.json.test.taobao;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.json.test.benchmark.decode.TradeParse;

import data.taobao.puti.GetHomePageData;
import data.taobao.puti.GetHomePageResponse;
import junit.framework.TestCase;

public class HomePageTest extends TestCase {
    private String text;
    
    protected void setUp() throws Exception {
        InputStream is = TradeParse.class.getClassLoader().getResourceAsStream("json/taobao/homepage_1.json");
        text = IOUtils.toString(is);
        is.close();
    }
    
    public void test_decode() throws Exception {
        GetHomePageResponse resp = JSON.parseObject(text, GetHomePageResponse.class);
        GetHomePageData data = resp.data;
        Assert.assertNotNull(data);
    }

    public void test_parse() throws Exception {
        JSONObject object = JSON.parseObject(text);
        System.out.println(JSON.toJSONString(object));
    }
}
