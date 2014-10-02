package com.alibaba.json.bvt;

import java.util.Calendar;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class CalendarTest extends TestCase {

    public void test_0() throws Exception {
        String text = "{\"calendar\":null}";
        
        VO vo = JSON.parseObject(text, VO.class);
        Assert.assertNull(vo.getCalendar());
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
