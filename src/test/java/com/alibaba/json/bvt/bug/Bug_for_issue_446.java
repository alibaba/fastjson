package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class Bug_for_issue_446 extends TestCase {

    public void test_for_issue() throws Exception {
        String text = "{\"amount\":1,\"channel_id\":\"wnys01\",\"gem\":1,\"id\":\"pay\",\"login_name\":\"U10722466A\",\"money\":1000,\"order_id\":\"99142AO10000086695A\",\"pay_channel\":\"weilan\",\"pay_time\":\"2015-11-05 20:59:04\",\"reward\":\"11:5_12:5_13:5,4:1_5:1_6:1\",\"status\":1,\"user_id\":19313}";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertEquals(1, obj.get("amount"));
    }

}
