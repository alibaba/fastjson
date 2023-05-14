package com.alibaba.json.bvt.issue_2600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Issue2606 extends TestCase {
    @Override
    public void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getDefault();
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_for_issue() throws Exception {
        String str = "2019-07-03 19:34:22,547";
        Date d = TypeUtils.castToDate(str);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        sdf.setTimeZone(TimeZone.getDefault());
        assertEquals(str, sdf.format(d));
    }
}