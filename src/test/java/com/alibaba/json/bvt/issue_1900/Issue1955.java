package com.alibaba.json.bvt.issue_1900;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Issue1955 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_for_issue() throws Exception {
        String strVal = "0100-01-27 11:22:00.000";
        Date date = JSON.parseObject('"' + strVal + '"', Date.class);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);
        df.setTimeZone(JSON.defaultTimeZone);

        assertEquals(df.parse(strVal), date);
    }
}
