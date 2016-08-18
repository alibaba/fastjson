package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class Bug_for_yanpei extends TestCase {
    public void test_for_sepcial_chars() throws Exception {
        String text = "{\"answerAllow\":true,\"atUsers\":[],\"desc\":\"Halios 1000M \\\"Puck\\\"很微众的品牌，几乎全靠玩家口口相传\"} ";
        JSONObject obj = JSON.parseObject(text);
        Assert.assertEquals(true, obj.get("answerAllow"));;
        Assert.assertEquals(0, obj.getJSONArray("atUsers").size());;
        Assert.assertEquals("Halios 1000M \"Puck\"很微众的品牌，几乎全靠玩家口口相传", obj.get("desc"));;
    }
}
