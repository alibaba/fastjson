package com.alibaba.json.bvt.serializer.date;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class DateTest_ISO8601_TimeZone extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    
    public void test_date1() throws Exception {
        Map<String,Date> map = new HashMap<String,Date>();
        map.put("date", new Date(1425886057586l));

        String json = JSON.toJSONString(map, SerializerFeature.UseISO8601DateFormat);
        
        Assert.assertEquals("{\"date\":\"2015-03-09T15:27:37.586+08:00\"}", json);

        Map<String,Date> newMap = JSON.parseObject(json, new TypeReference<Map<String,Date>>(){});

        Assert.assertEquals(1425886057586l, newMap.get("date").getTime());
    }

    public void test_date2() throws Exception {
        Calendar c = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
        c.setTimeZone(TimeZone.getTimeZone("GMT+10"));
        VO v = new VO();
        v.setGmtCreate(c);
        String json = JSON.toJSONString(v, SerializerFeature.UseISO8601DateFormat);
        System.out.println(json);

        Calendar cal = JSON.parseObject(json, VO.class).getGmtCreate();

        Assert.assertEquals(10, cal.getTimeZone().getRawOffset() / (3600 * 1000));
    }

    public void test_date3() throws Exception {
        Calendar c = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
        VO v = new VO();
        v.setGmtCreate(c);
        String json = JSON.toJSONString(v, SerializerFeature.UseISO8601DateFormat);
        System.out.println(json);

        Calendar cal = JSON.parseObject(json, VO.class).getGmtCreate();

        Assert.assertEquals(8, cal.getTimeZone().getRawOffset() / (3600 * 1000));
    }

    public static class VO {

        private Calendar gmtCreate;

        public Calendar getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(Calendar gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

    }
}
