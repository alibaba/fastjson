package com.alibaba.json.bvt.issue_1300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.Date;

public class Issue_for_zuojian extends TestCase {
    public void test_for_issue() throws Exception {
        JSON.DEFFAULT_DATE_FORMAT = "yyyyMMddHHmmssSSSZ";
        String json = "{\"value\":\"20180131022733000-0800\"}";
        JSON.parseObject(json, Model.class);
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    }

    public void test_for_issue_1() throws Exception {
        JSON.DEFFAULT_DATE_FORMAT = "yyyyMMddHHmmssSSSZ";

        String json = "{\"value\":\"20180131022733000-0800\"}";
        JSONObject object = JSON.parseObject(json);
        object.getObject("value", Date.class);

        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    }

    public static class Model {
        public Date value;
    }
}
