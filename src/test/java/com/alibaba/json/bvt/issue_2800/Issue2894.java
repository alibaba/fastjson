package com.alibaba.json.bvt.issue_2800;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Locale;
import java.util.TimeZone;

public class Issue2894 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getDefault();
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_for_issue() throws Exception {
        String json = "{\"timestamp\":\"2019-09-19 08:49:52.350000000\"}";
        Pojo pojo = JSONObject.parseObject(json, Pojo.class);
        int nanos = pojo.timestamp.getNanos();
        assertEquals(nanos, 350000000);
        assertEquals("{\"timestamp\":\"2019-09-19 08:49:52.000000350\"}", JSON.toJSONString(pojo));
    }

    public static class Pojo {
        @JSONField(name = "timestamp", format = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS")
        public Timestamp timestamp;
    }
}
