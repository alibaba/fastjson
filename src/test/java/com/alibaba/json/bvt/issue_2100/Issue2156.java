package com.alibaba.json.bvt.issue_2100;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue2156 extends TestCase {
    public void test_for_issue() throws Exception {
        java.sql.Date date = java.sql.Date.valueOf("2018-07-15");
        String str = JSON.toJSONStringWithDateFormat(date, JSON.DEFFAULT_DATE_FORMAT);
        assertEquals("\"2018-07-15\"", str);
    }
}
