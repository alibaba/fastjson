package com.alibaba.json.bvt.date;

import java.util.Calendar;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class CalendarTest extends TestCase {

    public void test_null() throws Exception {
        String text = "{\"calendar\":null}";
        
        VO vo = JSON.parseObject(text, VO.class);
        Assert.assertNull(vo.getCalendar());
    }
    
    public void test_codec() throws Exception {
        Calendar calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
        VO vo = new VO();
        vo.setCalendar(calendar);
        String text = JSON.toJSONString(vo);
        
        VO vo2 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo.getCalendar().getTimeInMillis(), vo2.getCalendar().getTimeInMillis());
    }
    
    public void test_codec_iso88591() throws Exception {
        Calendar calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
        VO vo = new VO();
        vo.setCalendar(calendar);
        String text = JSON.toJSONString(vo, SerializerFeature.UseISO8601DateFormat);
        
        VO vo2 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo.getCalendar().getTimeInMillis(), vo2.getCalendar().getTimeInMillis());
    }
    
    public void test_codec_iso88591_2() throws Exception {
        Calendar calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        VO vo = new VO();
        vo.setCalendar(calendar);
        String text = JSON.toJSONString(vo, SerializerFeature.UseISO8601DateFormat);
        
        VO vo2 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo.getCalendar().getTimeInMillis(), vo2.getCalendar().getTimeInMillis());
    }

    public static class VO {

        private Calendar calendar;

        public Calendar getCalendar() {
            return calendar;
        }

        public void setCalendar(Calendar calendar) {
            this.calendar = calendar;
        }

    }
}
