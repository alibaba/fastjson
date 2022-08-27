package com.alibaba.json.bvt.serializer;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;

import junit.framework.TestCase;

public class JSONSerializerTest3 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_0() throws Exception {
        JSONSerializer serializer = new JSONSerializer();

        serializer.setDateFormat("yyyy");
        Assert.assertEquals("yyyy", ((SimpleDateFormat) serializer.getDateFormat()).toPattern());
        Assert.assertEquals("yyyy",  serializer.getDateFormatPattern());
        
        serializer.setDateFormat("yyyy-MM");
        Assert.assertEquals("yyyy-MM", ((SimpleDateFormat) serializer.getDateFormat()).toPattern());
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(JSON.defaultTimeZone);
        serializer.setDateFormat(format);
        Assert.assertEquals("yyyy-MM-dd",  serializer.getDateFormatPattern());
        
        serializer.close();
    }

}
