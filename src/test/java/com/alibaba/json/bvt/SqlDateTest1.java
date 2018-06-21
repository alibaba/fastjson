package com.alibaba.json.bvt;

import java.sql.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class SqlDateTest1 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = new Locale("zh_CN");
    }
    
    public void test_date() throws Exception {
        long millis = 1324138987429L;
        Date date = new Date(millis);

        Assert.assertEquals("1324138987429", JSON.toJSONString(date));
        Assert.assertEquals("{\"@type\":\"java.sql.Date\",\"val\":1324138987429}", JSON.toJSONString(date, SerializerFeature.WriteClassName));
        Assert.assertEquals(1324138987429L, ((java.util.Date)JSON.parse("{\"@type\":\"java.util.Date\",\"val\":1324138987429}")).getTime());

        Assert.assertEquals("\"2011-12-18 00:23:07\"",
                            JSON.toJSONString(date, SerializerFeature.WriteDateUseDateFormat));
        Assert.assertEquals("\"2011-12-18 00:23:07.429\"",
                            JSON.toJSONStringWithDateFormat(date, "yyyy-MM-dd HH:mm:ss.SSS"));
        Assert.assertEquals("'2011-12-18 00:23:07.429'",
                            JSON.toJSONStringWithDateFormat(date, "yyyy-MM-dd HH:mm:ss.SSS",
                                                            SerializerFeature.UseSingleQuotes));
    }
}
