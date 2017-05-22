package com.alibaba.json.bvt.date;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class DateTest extends TestCase {

    public void test_date() throws Exception {
        long millis = 1324138987429L;
        Date date = new Date(millis);

        Assert.assertEquals("1324138987429", JSON.toJSONString(date));
        Assert.assertEquals("new Date(1324138987429)", JSON.toJSONString(date, SerializerFeature.WriteClassName));

        Assert.assertEquals("\"2011-12-18 00:23:07\"",
                            JSON.toJSONString(date, SerializerFeature.WriteDateUseDateFormat));
        Assert.assertEquals("\"2011-12-18 00:23:07.429\"",
                            JSON.toJSONStringWithDateFormat(date, "yyyy-MM-dd HH:mm:ss.SSS"));
        Assert.assertEquals("'2011-12-18 00:23:07.429'",
                            JSON.toJSONStringWithDateFormat(date, "yyyy-MM-dd HH:mm:ss.SSS",
                                                            SerializerFeature.UseSingleQuotes));
    }
}
