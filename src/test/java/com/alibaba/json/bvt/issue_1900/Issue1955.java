package com.alibaba.json.bvt.issue_1900;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Issue1955 extends TestCase {
    public void test_for_issue() throws Exception {
        String strVal = "0100-01-27 11:22:00.000";
        Date date = JSON.parseObject('"' + strVal + '"', Date.class);

        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strVal), date);
    }
}
