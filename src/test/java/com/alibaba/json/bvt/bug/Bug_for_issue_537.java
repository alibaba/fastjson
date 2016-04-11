package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import junit.framework.TestCase;

public class Bug_for_issue_537 extends TestCase {

    public void test_emptyIgnore() throws Exception {
        String text = "{\"value\":2147483649}";
        Exception error = null;
        try {
            JSON.parseObject(text, VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        Assert.assertTrue(error.getMessage().contains("field : value"));
    }

    public static class VO {

        public int value;
    }
}
