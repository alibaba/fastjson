package com.alibaba.json.bvt;


import java.sql.Timestamp;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TimestampTest extends TestCase {
    public void test_0 () throws Exception {
        long millis = System.currentTimeMillis();
        
        Assert.assertEquals(new Timestamp(millis), JSON.parseObject("" + millis, Timestamp.class));
        Assert.assertEquals(new Timestamp(millis), JSON.parseObject("\"" + millis + "\"", Timestamp.class));
    }
}
