package com.alibaba.json.bvt.issue_1300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import junit.framework.TestCase;

import java.math.BigDecimal;

/**
 * Created by wenshao on 30/07/2017.
 */
public class Issue1330_decimal extends TestCase {
    public void test_for_issue() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":\"中ABC\"}", Model.class);
        } catch (JSONException e) {
            error = e;
        }
        assertNotNull(error);
        assertTrue(error.getMessage().indexOf("parseDecimal error, field : value") != -1);
    }

    public void test_for_issue_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":[]}", Model.class);
        } catch (JSONException e) {
            error = e;
        }
        assertNotNull(error);
        assertTrue(error.getMessage().indexOf("parseDecimal error, field : value") != -1);
    }

    public void test_for_issue_2() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":{\"xx\":[]}}", Model.class);
        } catch (JSONException e) {
            error = e;
        }
        assertNotNull(error);
        assertTrue(error.getMessage().indexOf("parseDecimal error, field : value") != -1);
    }

    public static class Model {
        public BigDecimal value;
    }
}
