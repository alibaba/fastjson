package com.alibaba.json.bvt.serializer;

import java.util.TimeZone;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TimeZoneTest extends TestCase {

    public void test_timezone() throws Exception {
        TimeZone tz1 = TimeZone.getDefault();
        String text = JSON.toJSONString(tz1);

        Assert.assertEquals(JSON.toJSONString(tz1.getID()), text);
        
        TimeZone tz2 = JSON.parseObject(text, TimeZone.class);
        Assert.assertEquals(tz1.getID(), tz2.getID());
    }
}
