package com.alibaba.json.bvt.parser.deser.date;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.CalendarCodec;
import com.alibaba.json.bvt.parser.deser.date.DateParseTest14.VO;


public class DateParseTest9 extends TestCase {
    public void test_date() throws Exception {
        String text = "\"/Date(1242357713797+0800)/\"";
        Date date = JSON.parseObject(text, Date.class);
        Assert.assertEquals(date.getTime(), 1242357713797L);
        
        Assert.assertEquals(JSONToken.LITERAL_INT, CalendarCodec.instance.getFastMatchToken());
    }
    
    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"date\":\"/Date(1242357713797A0800)/\"}", VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"date\":\"/Date(1242357713797#0800)/\"}", VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
