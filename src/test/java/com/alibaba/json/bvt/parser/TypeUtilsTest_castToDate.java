package com.alibaba.json.bvt.parser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;

public class TypeUtilsTest_castToDate extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_castToDate() throws Exception {
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
        Date date = TypeUtils.castToDate("2012-07-15 12:12:11");
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(JSON.defaultTimeZone);
        Assert.assertEquals(format.parseObject("2012-07-15 12:12:11"), date);
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    }

    public void test_castToDate_error() throws Exception {
        Exception error = null;
        try {
            TypeUtils.castToDate("你妈你妈-MM-dd");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_castToDate_zero() throws Exception {
        Assert.assertEquals(new Date(0), TypeUtils.castToDate("0"));
    }

    public void test_castToDate_negative() throws Exception {
        Assert.assertEquals(new Date(-1), TypeUtils.castToDate(-1));
    }

}
