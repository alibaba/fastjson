package com.alibaba.json.bvt.bug;

import java.util.Calendar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.Assert;
import junit.framework.TestCase;

public class Bug_for_cnhans extends TestCase {

    public void test_0() throws Exception {
        VO vo = new VO();
        vo.setCalendar(Calendar.getInstance());

        String text = JSON.toJSONString(vo);

        VO vo1 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo.getCalendar().getTime(), vo1.getCalendar().getTime());
    }
    
    public void test_format() throws Exception {
        VO vo = new VO();
        vo.setCalendar(Calendar.getInstance());
        
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
