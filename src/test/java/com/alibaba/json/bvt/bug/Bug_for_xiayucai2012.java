package com.alibaba.json.bvt.bug;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class Bug_for_xiayucai2012 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_for_xiayucai2012() throws Exception {
        String text = "{\"date\":\"0000-00-00 00:00:00\"}";
        JSONObject json = JSON.parseObject(text);
        Date date = json.getObject("date", Date.class);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(JSON.DEFFAULT_DATE_FORMAT, JSON.defaultLocale);
        dateFormat.setTimeZone(JSON.defaultTimeZone);
        
        Assert.assertEquals(dateFormat.parse(json.getString("date")), date);
    }
}
