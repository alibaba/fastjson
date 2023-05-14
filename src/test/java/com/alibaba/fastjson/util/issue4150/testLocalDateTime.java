package com.alibaba.fastjson.util.issue4150;

import com.alibaba.fastjson.util.TypeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class testLocalDateTime {
    @Test
    public void testLocal(){
        LocalDateTime a = LocalDateTime.now();
        Date d = TypeUtils.castToDate(a);
        Assert.assertEquals((new Date().toString()),d.toString());
    }
    @Test
    public void testWithZone(){
        LocalDateTime a = LocalDateTime.now(ZoneId.of("America/Los_Angeles"));
        Date d = TypeUtils.castToDate(a);
        Assert.assertEquals(a.getHour(),d.getHours());
        Assert.assertEquals(a.getDayOfMonth(),d.getDate());
    }
}