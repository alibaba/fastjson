package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;
import org.junit.Assert;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.TimeZone;

public class SqlTimestampTest
        extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = new Locale("zh_CN");
    }
    
    public void test_date() throws Exception {
        Timestamp ts = new Timestamp(
                97,
                2,
                17,
                15,
                53,
                01,
                12345678
        );

        System.out.println('"' + ts.toString() + '"');

        String json = JSON.toJSONString(ts, SerializerFeature.UseISO8601DateFormat);
        assertEquals('"' + ts.toString() + '"', '"' + ts.toString() + '"');

        Timestamp ts2 = JSON.parseObject(json, Timestamp.class);
        String json2 = JSON.toJSONString(ts2, SerializerFeature.UseISO8601DateFormat);
        assertEquals(json, json2);
    }
}
