package com.alibaba.json.bvt.bug;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_cnhans extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_0() throws Exception {
        VO vo = new VO();
        vo.setCalendar(Calendar.getInstance());

        String text = JSON.toJSONString(vo);

        VO vo1 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo.getCalendar().getTime(), vo1.getCalendar().getTime());
    }
    
    public void test_format() throws Exception {
        VO vo = new VO();
        vo.setCalendar(Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale));
        
        String text = JSON.toJSONString(vo, SerializerFeature.WriteDateUseDateFormat);
        
        VO vo1 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo.getCalendar().get(Calendar.YEAR), vo1.getCalendar().get(Calendar.YEAR));
        Assert.assertEquals(vo.getCalendar().get(Calendar.MONTH), vo1.getCalendar().get(Calendar.MONTH));
        Assert.assertEquals(vo.getCalendar().get(Calendar.DAY_OF_MONTH), vo1.getCalendar().get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(vo.getCalendar().get(Calendar.HOUR_OF_DAY), vo1.getCalendar().get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(vo.getCalendar().get(Calendar.MINUTE), vo1.getCalendar().get(Calendar.MINUTE));
        Assert.assertEquals(vo.getCalendar().get(Calendar.SECOND), vo1.getCalendar().get(Calendar.SECOND));
    }
    
    public void test_iso_format() throws Exception {
        VO vo = new VO();
        vo.setCalendar(Calendar.getInstance());
        
        String text = JSON.toJSONString(vo, SerializerFeature.UseISO8601DateFormat);
        
        VO vo1 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo.getCalendar().get(Calendar.YEAR), vo1.getCalendar().get(Calendar.YEAR));
        Assert.assertEquals(vo.getCalendar().get(Calendar.MONTH), vo1.getCalendar().get(Calendar.MONTH));
        Assert.assertEquals(vo.getCalendar().get(Calendar.DAY_OF_MONTH), vo1.getCalendar().get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(vo.getCalendar().get(Calendar.HOUR_OF_DAY), vo1.getCalendar().get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(vo.getCalendar().get(Calendar.MINUTE), vo1.getCalendar().get(Calendar.MINUTE));
        Assert.assertEquals(vo.getCalendar().get(Calendar.SECOND), vo1.getCalendar().get(Calendar.SECOND));
    }
    
    public void test_toJavaObject() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("d1", new Date());
        obj.put("d2", System.currentTimeMillis());
        obj.put("d3", GregorianCalendar.getInstance());
        obj.put("d4", "2012-12-22");
        obj.put("d5", "2012-12-22 12:11:11");
        obj.put("d6", "2012-12-22 12:11:11.234");
        
        obj.getObject("d1", Calendar.class);
        obj.getObject("d2", Calendar.class);
        obj.getObject("d3", Calendar.class);
        obj.getObject("d4", Calendar.class);
        obj.getObject("d5", Calendar.class);
        obj.getObject("d6", Calendar.class);

        obj.getObject("d1", GregorianCalendar.class);
        obj.getObject("d2", GregorianCalendar.class);
        obj.getObject("d3", GregorianCalendar.class);
        obj.getObject("d4", GregorianCalendar.class);
        obj.getObject("d5", GregorianCalendar.class);
        obj.getObject("d6", GregorianCalendar.class);
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
