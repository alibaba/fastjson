package com.alibaba.json.bvt.parser.deser;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.CalendarDeserializer;


public class DateParseTest9 extends TestCase {
    public void test_date() throws Exception {
        String text = "\"/Date(1242357713797+0800)/\"";
        Date date = JSON.parseObject(text, Date.class);
        Assert.assertEquals(date.getTime(), 1242357713797L);
        
        Assert.assertEquals(JSONToken.LITERAL_INT, CalendarDeserializer.instance.getFastMatchToken());
    }
}
