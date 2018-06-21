package com.alibaba.json.bvt.date;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;

import junit.framework.TestCase;

public class DateTest_tz extends TestCase {
//    protected void setUp() throws Exception {
//        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
//        JSON.defaultLocale = Locale.CHINA;
//    }
    
    public void test_codec() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"value\":\"2016-04-29\"}"));
        reader.setLocale(Locale.CHINA);
        reader.setTimzeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        
        Model model = reader.readObject(Model.class);
        Assert.assertNotNull(model.value);
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date date = format.parse("2016-04-29");
        Assert.assertEquals(date.getTime(), model.value.getTime());
        
        Assert.assertEquals(TimeZone.getTimeZone("Asia/Shanghai"), reader.getTimzeZone());
        Assert.assertEquals(Locale.CHINA, reader.getLocal());
        
        reader.close();
    }
    
    public static class Model {
        public Date value;
    }
}
