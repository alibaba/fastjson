package com.alibaba.json.bvt;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TimestampTest extends TestCase {
    public void test_0 () throws Exception {
        long millis = (System.currentTimeMillis() / 1000) * 1000;
        
        String text = "\"" + new SimpleDateFormat(JSON.DEFFAULT_DATE_FORMAT).format(new Date(millis)) + "\"";
        System.out.println(text);
        Assert.assertEquals(new Timestamp(millis), JSON.parseObject("" + millis, Timestamp.class));
        Assert.assertEquals(new Timestamp(millis), JSON.parseObject("\"" + millis + "\"", Timestamp.class));
        Assert.assertEquals(new Timestamp(millis), JSON.parseObject(text, Timestamp.class));
        Assert.assertEquals(new java.sql.Date(millis), JSON.parseObject(text, java.sql.Date.class));
        Assert.assertEquals(new java.util.Date(millis), JSON.parseObject(text, java.util.Date.class));
    }
}
