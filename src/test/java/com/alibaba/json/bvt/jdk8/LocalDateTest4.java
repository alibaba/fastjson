package com.alibaba.json.bvt.jdk8;

import java.time.LocalDate;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class LocalDateTest4 extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"20160506\"}", VO.class);
        
        Assert.assertEquals(2016, vo.date.getYear());
        Assert.assertEquals(5, vo.date.getMonthValue());
        Assert.assertEquals(6, vo.date.getDayOfMonth());
    }

    public static class VO {
        public LocalDate date;

    }
}
