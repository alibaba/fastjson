package com.alibaba.json.bvt;

import java.sql.Date;
import java.text.SimpleDateFormat;
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
//
//    public void test_date2() throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        java.util.Date d = sdf.parse("2019-09-12 16:00:00");
//        java.sql.Date ds = new java.sql.Date(d.getTime());
////        System.out.println("Java Obj: " + sdf.format(ds));
//
//        String jvs = JSON.toJSONString(ds);
////        System.out.println("JSON Str: " + jvs);
//
//        java.sql.Date d2s = JSON.parseObject(jvs, java.sql.Date.class);
////        System.out.println("Java Obj: " + sdf.format(d2s));
////        System.out.println("LONG: " + d2s.getTime());
//
//        assertEquals(d.getTime(), d2s.getTime());
//    }
}
