package com.alibaba.json.bvt.issue_1900;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.Date;
import java.util.Locale;

public class Issue1901 extends TestCase {
    protected Locale locale;
    protected void setUp() throws Exception {
        locale = JSON.defaultLocale;
        JSON.defaultLocale = Locale.CHINA;
    }

    protected void tearDown() throws Exception {
        JSON.defaultLocale = locale;
    }

    public void test_for_issue() throws Exception {
        Model m = JSON.parseObject("{\"time\":\"Thu Mar 22 08:58:37 +0000 2018\"}", Model.class);
        assertEquals("{\"time\":\"星期四 三月 22 16:58:37 CST 2018\"}", JSON.toJSONString(m));
    }

    public void test_for_issue_1() throws Exception {
        Model m = JSON.parseObject("{\"time\":\"星期四 三月 22 16:58:37 CST 2018\"}", Model.class);
        assertEquals("{\"time\":\"星期四 三月 22 16:58:37 CST 2018\"}", JSON.toJSONString(m));
    }

    public static class Model {
        @JSONField(format = "EEE MMM dd HH:mm:ss zzz yyyy")
        public Date time;
    }
}
