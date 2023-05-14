package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONObject;

public class Bug_for_gongwenhua extends TestCase {

    public void test_0() throws Exception {
        String text = "{\"FH2\\\"\u0005\\v\u0010\u000e\u0011\u0000\":0,\"alipa9_login\":0,\"alipay_login\":14164,\"durex\":317,\"intl.datasky\":0,\"taobao_refund\":880}";

        JSONObject obj = JSONObject.parseObject(text);
        Assert.assertNotNull(obj);
        Assert.assertEquals(0, obj.get("FH2\"\u0005\u000B\u0010\u000e\u0011\u0000"));

    }

}
