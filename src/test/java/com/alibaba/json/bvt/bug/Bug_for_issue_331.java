package com.alibaba.json.bvt.bug;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Bug_for_issue_331 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_for_issue() throws Exception {
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", JSON.defaultLocale);
        format.setTimeZone(JSON.defaultTimeZone);
        Date date = format.parse("2015-05-23");

        Calendar c = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
        c.setTime(date);

        Model original = new Model();
        original.setDate(date);
        original.setCalendar(c);
        
        String json = JSON.toJSONString(original, SerializerFeature.UseISO8601DateFormat);

        System.out.println(json); //V1.2.4 输出{"calendar":"2015-05-23","date":"2015-05-23"} , V1.2.6 输出{"calendar":"2015-05-23+08:00","date":"2015-05-23+08:00"}

        Model actual = JSON.parseObject(json, Model.class);

        Assert.assertNotNull(actual);
        Assert.assertNotNull(actual.getDate());
        Assert.assertNotNull(actual.getCalendar());

        Assert.assertEquals("与序列化前比较不相等", original.getDate(), actual.getDate());

        Assert.assertEquals("序列化后的Date 和 Calendar 不相等", actual.getDate(), actual.getCalendar().getTime());
    }
    
    public static class Model {
        private Date date;
        private Calendar calendar;
        
        public Date getDate() {
            return date;
        }
        
        public void setDate(Date date) {
            this.date = date;
        }
        
        public Calendar getCalendar() {
            return calendar;
        }
        
        public void setCalendar(Calendar calendar) {
            this.calendar = calendar;
        }
        
        
    }
}
