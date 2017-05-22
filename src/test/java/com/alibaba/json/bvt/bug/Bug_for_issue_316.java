package com.alibaba.json.bvt.bug;

import java.sql.Timestamp;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_316 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.value = new Timestamp(1460563200000L);
        Assert.assertEquals("{\"value\":1460563200000}", JSON.toJSONString(model));
    }

    public static class Model {
        public java.sql.Timestamp value;
    }
}
