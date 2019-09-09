package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by wenshao on 10/01/2017.
 */
public class Issue978 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.date = new java.util.Date(1483413683714L);

        JSONObject obj = (JSONObject) JSON.toJSON(model);
        assertEquals("{\"date\":\"2017-01-03 11:21:23\"}", obj.toJSONString());
    }

    public void test_for_issue2() throws Exception {
        Model model = new Model();
        model.date = new java.sql.Date(1483413683714L);

        JSONObject obj = (JSONObject) JSON.toJSON(model);
        assertEquals("{\"date\":\"2017-01-03 11:21:23\"}", obj.toJSONString());
    }

    public static class Model {
        @JSONField(format="yyyy-MM-dd HH:mm:ss")
        public Date date;
    }
}
