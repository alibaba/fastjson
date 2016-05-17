package com.alibaba.json.bvt.date;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class DateFieldTest10 extends TestCase {

    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_tw() throws Exception {
        Entity vo = JSON.parseObject("{\"date\":\"2016/05/06 09:03:16\"}", Entity.class);

        Calendar calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
        calendar.setTime(vo.date);
        Assert.assertEquals(2016, calendar.get(Calendar.YEAR));
        Assert.assertEquals(4, calendar.get(Calendar.MONTH));
        Assert.assertEquals(6, calendar.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(9, calendar.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(3, calendar.get(Calendar.MINUTE));
        Assert.assertEquals(16, calendar.get(Calendar.SECOND));
        Assert.assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }
    
    public void test_cn() throws Exception {
        Entity vo = JSON.parseObject("{\"date\":\"2016-05-06 09:03:16\"}", Entity.class);

        Calendar calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
        calendar.setTime(vo.date);
        Assert.assertEquals(2016, calendar.get(Calendar.YEAR));
        Assert.assertEquals(4, calendar.get(Calendar.MONTH));
        Assert.assertEquals(6, calendar.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(9, calendar.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(3, calendar.get(Calendar.MINUTE));
        Assert.assertEquals(16, calendar.get(Calendar.SECOND));
        Assert.assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    public void test_de() throws Exception {
        Entity vo = JSON.parseObject("{\"date\":\"06.05.2016 09:03:16\"}", Entity.class);

        Calendar calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
        calendar.setTime(vo.date);
        Assert.assertEquals(2016, calendar.get(Calendar.YEAR));
        Assert.assertEquals(4, calendar.get(Calendar.MONTH));
        Assert.assertEquals(6, calendar.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(9, calendar.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(3, calendar.get(Calendar.MINUTE));
        Assert.assertEquals(16, calendar.get(Calendar.SECOND));
        Assert.assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }
    
    public void test_in() throws Exception {
        Entity vo = JSON.parseObject("{\"date\":\"06-05-2016 09:03:16\"}", Entity.class);

        Calendar calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
        calendar.setTime(vo.date);
        Assert.assertEquals(2016, calendar.get(Calendar.YEAR));
        Assert.assertEquals(4, calendar.get(Calendar.MONTH));
        Assert.assertEquals(6, calendar.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(9, calendar.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(3, calendar.get(Calendar.MINUTE));
        Assert.assertEquals(16, calendar.get(Calendar.SECOND));
        Assert.assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    public static class Entity {

        public Date date;
    }
}
