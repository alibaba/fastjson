package com.alibaba.json.bvt.parser.deser;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;


public class DateParseTest6 extends TestCase {
    public void test_date() throws Exception {
        String text = "\"19790714\"";
        Date date = JSON.parseObject(text, Date.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        Assert.assertEquals(1979, calendar.get(Calendar.YEAR));
        Assert.assertEquals(6, calendar.get(Calendar.MONTH));
        Assert.assertEquals(14, calendar.get(Calendar.DAY_OF_MONTH));
        
        Assert.assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(0, calendar.get(Calendar.MINUTE));
        Assert.assertEquals(0, calendar.get(Calendar.SECOND));
        Assert.assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }
}
