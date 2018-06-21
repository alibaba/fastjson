package com.alibaba.json.bvt.parser.deser.date;

import java.util.Date;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class DateDeserializerTest extends TestCase {

    public void test_date() throws Exception {
        long millis = System.currentTimeMillis();
        Assert.assertEquals(new Date(millis), JSON.parseObject("'" + millis + "'", Date.class));
    }
}
