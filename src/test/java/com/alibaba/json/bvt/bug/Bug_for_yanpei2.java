package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class Bug_for_yanpei2 extends TestCase {
    public void test_for_sepcial_chars() throws Exception {
        String text = "{\"answerAllow\":true,\"atUsers\":[],\"desc\":\"测试账号\\n测试账号\"}";
        JSONObject obj = JSON.parseObject(text);
        Assert.assertEquals(true, obj.get("answerAllow"));;
        Assert.assertEquals(0, obj.getJSONArray("atUsers").size());;
        Assert.assertEquals("测试账号\n测试账号", obj.get("desc"));;
    }
}
