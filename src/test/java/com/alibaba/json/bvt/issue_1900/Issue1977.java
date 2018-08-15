package com.alibaba.json.bvt.issue_1900;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Issue1977 extends TestCase {
    public void test_for_issue() throws Exception {
        java.sql.Date date = new java.sql.Date(1533265119604L);
        String json = JSON.toJSONString(date, SerializerFeature.UseISO8601DateFormat);
        assertEquals("\"2018-08-03T10:58:39.604+08:00\"", json);
//        new java.sql.Date();
    }
}
