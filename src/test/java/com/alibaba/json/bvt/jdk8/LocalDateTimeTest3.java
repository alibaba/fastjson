package com.alibaba.json.bvt.jdk8;

import java.time.LocalDateTime;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class LocalDateTimeTest3 extends TestCase {

    public void test_for_issue() throws Exception {
        String text = "{\"date\":\"20111203\"}";
        VO vo = JSON.parseObject(text, VO.class);
        
        Assert.assertEquals(2011, vo.date.getYear());
        Assert.assertEquals(12, vo.date.getMonthValue());
        Assert.assertEquals(03, vo.date.getDayOfMonth());
        Assert.assertEquals(0, vo.date.getHour());
        Assert.assertEquals(0, vo.date.getMinute());
        Assert.assertEquals(0, vo.date.getSecond());
        
        Assert.assertEquals(text, JSON.toJSONString(vo));
    }

    public static class VO {
        @JSONField(format="yyyyMMdd")
        public LocalDateTime date;

    }
}
