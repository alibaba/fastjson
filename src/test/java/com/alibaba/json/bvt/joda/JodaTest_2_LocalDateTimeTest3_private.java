package com.alibaba.json.bvt.joda;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.junit.Assert;

import org.joda.time.LocalDateTime;

public class JodaTest_2_LocalDateTimeTest3_private extends TestCase {

    public void test_for_issue() throws Exception {
        String text = "{\"date\":\"20111203\"}";
        VO vo = JSON.parseObject(text, VO.class);
        
        assertEquals(2011, vo.date.getYear());
        assertEquals(12, vo.date.getMonthOfYear());
        assertEquals(03, vo.date.getDayOfMonth());
        assertEquals(0, vo.date.getHourOfDay());
        assertEquals(0, vo.date.getMinuteOfHour());
        assertEquals(0, vo.date.getSecondOfMinute());
        
        assertEquals(text, JSON.toJSONString(vo));
    }

    private static class VO {
        @JSONField(format="yyyyMMdd")
        public LocalDateTime date;

    }
}
