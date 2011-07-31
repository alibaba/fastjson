package com.alibaba.json.test.bvt.parser.deser;

import java.util.Date;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class DateDeserializerTest extends TestCase {

    public void test_date() throws Exception {
        long millis = System.currentTimeMillis();
        Assert.assertEquals(new Date(millis), JSON.parseObject("'" + millis + "'", Date.class));
    }
}
