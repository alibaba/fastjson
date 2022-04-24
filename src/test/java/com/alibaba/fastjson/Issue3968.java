package com.alibaba.fastjson;

import org.junit.Assert;
import org.junit.Test;

//CS304 Issue link: https://github.com/alibaba/fastjson/issues/4013
/**
 * A Test on Issue3968
 **/
public class Issue3968 {

    /**
     * the key string
     */
    private final static String KEY = "rank";

    /**
     * test if JSON object contains integer in from of scientific notations
     * can be generated into correct strings in form of scientific notation
     **/
    @Test
    public void testInt() {
        final String message = "test integer in form of scientific notation";
        final String str1 = "{\"rank\": 2.4E10}";
        final String str2 = "{\"rank\": -2.4E10}";
        final String str3 = "{\"rank\": 2.4E1}";
        final String str4 = "{\"rank\": 2.444E3}";
        final String str5 = "{\"rank\": 1E0}";
        final JSONObject jo1 = JSON.parseObject(str1);
        final JSONObject jo2 = JSON.parseObject(str2);
        final JSONObject jo3 = JSON.parseObject(str3);
        final JSONObject jo4 = JSON.parseObject(str4);
        final JSONObject jo5 = JSON.parseObject(str5);
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo1, KEY, true), "{\"rank\":\"2.4E10\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo2, KEY, true), "{\"rank\":\"-2.4E10\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo3, KEY, true), "{\"rank\":\"2.4E1\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo4, KEY, true), "{\"rank\":\"2.444E3\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo5, KEY, true), "{\"rank\":\"1E0\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo1, KEY), "{\"rank\":\"2.4E10\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo2, KEY), "{\"rank\":\"-2.4E10\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo3, KEY), "{\"rank\":\"2.4E1\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo4, KEY), "{\"rank\":\"2.444E3\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo5, KEY), "{\"rank\":\"1E0\"}");
    }

    /**
     * test if JSON object contains double in from of scientific notations
     * can be generated into correct strings in form of scientific notation
     **/
    @Test
    public void testDouble() {
        final String message = "test double in form of scientific notation";
        final String str1 = "{\"rank\": 2.4E-10}";
        final String str2 = "{\"rank\": -2.012345E-10}";
        final String str3 = "{\"rank\": 2.4E-1}";
        final String str4 = "{\"rank\": 2.444E-3}";
        final String str5 = "{\"rank\": 1E-1}";
        final JSONObject jo1 = JSON.parseObject(str1);
        final JSONObject jo2 = JSON.parseObject(str2);
        final JSONObject jo3 = JSON.parseObject(str3);
        final JSONObject jo4 = JSON.parseObject(str4);
        final JSONObject jo5 = JSON.parseObject(str5);
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo1, KEY, true), "{\"rank\":\"2.4E-10\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo2, KEY, true), "{\"rank\":\"-2.012345E-10\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo3, KEY, true), "{\"rank\":\"2.4E-1\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo4, KEY, true), "{\"rank\":\"2.444E-3\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo5, KEY, true), "{\"rank\":\"1E-1\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo1, KEY), "{\"rank\":\"2.4E-10\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo2, KEY), "{\"rank\":\"-2.012345E-10\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo3, KEY), "{\"rank\":\"2.4E-1\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo4, KEY), "{\"rank\":\"2.444E-3\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo5, KEY), "{\"rank\":\"1E-1\"}");
    }

    /**
     * test if JSON object contains integer in from of scientific notations
     * can be generated into correct strings in form of plain number
     **/

    @Test
    public void testIntPlain() {
        final String message = "test integer in form of plain number";
        final String str1 = "{\"rank\": 2.4E10}";
        final String str2 = "{\"rank\": -2.4E10}";
        final String str3 = "{\"rank\": 2.4E1}";
        final String str4 = "{\"rank\": 2.444E3}";
        final String str5 = "{\"rank\": 1E0}";
        final JSONObject jo1 = JSON.parseObject(str1);
        final JSONObject jo2 = JSON.parseObject(str2);
        final JSONObject jo3 = JSON.parseObject(str3);
        final JSONObject jo4 = JSON.parseObject(str4);
        final JSONObject jo5 = JSON.parseObject(str5);
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo1, KEY, false), "{\"rank\":\"24000000000\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo2, KEY, false), "{\"rank\":\"-24000000000\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo3, KEY, false), "{\"rank\":\"24\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo4, KEY, false), "{\"rank\":\"2444\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo5, KEY, false), "{\"rank\":\"1\"}");
    }

    /**
     * test if JSON object contains double in from of scientific notations
     * can be generated into correct strings in form of plain number
     **/
    @Test
    public void testDoublePlain() {
        final String message = "test double in form of plain number";
        final String str1 = "{\"rank\": 2.4E-10}";
        final String str2 = "{\"rank\": -2.012345E-10}";
        final String str3 = "{\"rank\": 2.4E-1}";
        final String str4 = "{\"rank\": 2.444E-3}";
        final String str5 = "{\"rank\": 1E-1}";
        final JSONObject jo1 = JSON.parseObject(str1);
        final JSONObject jo2 = JSON.parseObject(str2);
        final JSONObject jo3 = JSON.parseObject(str3);
        final JSONObject jo4 = JSON.parseObject(str4);
        final JSONObject jo5 = JSON.parseObject(str5);
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo1, KEY, false), "{\"rank\":\"0.00000000024\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo2, KEY, false), "{\"rank\":\"-0.0000000002012345\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo3, KEY, false), "{\"rank\":\"0.24\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo4, KEY, false), "{\"rank\":\"0.002444\"}");
        Assert.assertEquals(message, JSON.toJSONStringScientificNotation(jo5, KEY, false), "{\"rank\":\"0.1\"}");
    }
}
