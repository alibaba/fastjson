package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class Issue3968 {

    @Test
    public void testInt() {
        String str1 = "{\"rank\": 2.4E10}";
        String str2 = "{\"rank\": -2.4E10}";
        String str3 = "{\"rank\": 2.4E1}";
        String str4 = "{\"rank\": 2.444E3}";
        String str5 = "{\"rank\": 1E0}";
        JSONObject jo1 = JSON.parseObject(str1);
        JSONObject jo2 = JSON.parseObject(str2);
        JSONObject jo3 = JSON.parseObject(str3);
        JSONObject jo4 = JSON.parseObject(str4);
        JSONObject jo5 = JSON.parseObject(str5);
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo1, "rank", true), "{\"rank\":\"2.4E10\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo2, "rank", true), "{\"rank\":\"-2.4E10\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo3, "rank", true), "{\"rank\":\"2.4E1\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo4, "rank", true), "{\"rank\":\"2.444E3\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo5, "rank", true), "{\"rank\":\"1E0\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo1, "rank"), "{\"rank\":\"2.4E10\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo2, "rank"), "{\"rank\":\"-2.4E10\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo3, "rank"), "{\"rank\":\"2.4E1\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo4, "rank"), "{\"rank\":\"2.444E3\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo5, "rank"), "{\"rank\":\"1E0\"}");
    }

    @Test
    public void testDouble() {
        String str1 = "{\"rank\": 2.4E-10}";
        String str2 = "{\"rank\": -2.012345E-10}";
        String str3 = "{\"rank\": 2.4E-1}";
        String str4 = "{\"rank\": 2.444E-3}";
        String str5 = "{\"rank\": 1E-1}";
        JSONObject jo1 = JSON.parseObject(str1);
        JSONObject jo2 = JSON.parseObject(str2);
        JSONObject jo3 = JSON.parseObject(str3);
        JSONObject jo4 = JSON.parseObject(str4);
        JSONObject jo5 = JSON.parseObject(str5);
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo1, "rank", true), "{\"rank\":\"2.4E-10\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo2, "rank", true), "{\"rank\":\"-2.012345E-10\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo3, "rank", true), "{\"rank\":\"2.4E-1\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo4, "rank", true), "{\"rank\":\"2.444E-3\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo5, "rank", true), "{\"rank\":\"1E-1\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo1, "rank"), "{\"rank\":\"2.4E-10\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo2, "rank"), "{\"rank\":\"-2.012345E-10\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo3, "rank"), "{\"rank\":\"2.4E-1\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo4, "rank"), "{\"rank\":\"2.444E-3\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo5, "rank"), "{\"rank\":\"1E-1\"}");
    }

    @Test
    public void testIntPlain() {
        String str1 = "{\"rank\": 2.4E10}";
        String str2 = "{\"rank\": -2.4E10}";
        String str3 = "{\"rank\": 2.4E1}";
        String str4 = "{\"rank\": 2.444E3}";
        String str5 = "{\"rank\": 1E0}";
        JSONObject jo1 = JSON.parseObject(str1);
        JSONObject jo2 = JSON.parseObject(str2);
        JSONObject jo3 = JSON.parseObject(str3);
        JSONObject jo4 = JSON.parseObject(str4);
        JSONObject jo5 = JSON.parseObject(str5);
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo1, "rank", false), "{\"rank\":\"24000000000\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo2, "rank", false), "{\"rank\":\"-24000000000\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo3, "rank", false), "{\"rank\":\"24\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo4, "rank", false), "{\"rank\":\"2444\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo5, "rank", false), "{\"rank\":\"1\"}");
    }

    @Test
    public void testDoublePlain() {
        String str1 = "{\"rank\": 2.4E-10}";
        String str2 = "{\"rank\": -2.012345E-10}";
        String str3 = "{\"rank\": 2.4E-1}";
        String str4 = "{\"rank\": 2.444E-3}";
        String str5 = "{\"rank\": 1E-1}";
        JSONObject jo1 = JSON.parseObject(str1);
        JSONObject jo2 = JSON.parseObject(str2);
        JSONObject jo3 = JSON.parseObject(str3);
        JSONObject jo4 = JSON.parseObject(str4);
        JSONObject jo5 = JSON.parseObject(str5);
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo1, "rank", false), "{\"rank\":\"0.00000000024\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo2, "rank", false), "{\"rank\":\"-0.0000000002012345\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo3, "rank", false), "{\"rank\":\"0.24\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo4, "rank", false), "{\"rank\":\"0.002444\"}");
        Assert.assertEquals(JSON.toJSONStringScientificNotation(jo5, "rank", false), "{\"rank\":\"0.1\"}");
    }
}
