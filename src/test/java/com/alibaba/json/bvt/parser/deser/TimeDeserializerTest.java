package com.alibaba.json.bvt.parser.deser;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class TimeDeserializerTest extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_time() throws Exception {
        long millis = System.currentTimeMillis();
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", JSON.defaultLocale);
        format.setTimeZone(JSON.defaultTimeZone);
        String text = format.format(new java.util.Date(millis));
        text += "T";
        
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss.SSS", JSON.defaultLocale);
        format2.setTimeZone(JSON.defaultTimeZone);
        text += format2.format(new java.util.Date(millis));
        
        Assert.assertNull(JSON.parseObject("null", Time.class));
        Assert.assertNull(JSON.parseObject("\"\"", Time.class));
        Assert.assertNull(JSON.parseArray("null", Time.class));
        Assert.assertNull(JSON.parseArray("[null]", Time.class).get(0));
        Assert.assertNull(JSON.parseObject("{\"value\":null}", VO.class).getValue());
        
        Assert.assertEquals(new Time(millis), JSON.parseObject("" + millis, Time.class));
        Assert.assertEquals(new Time(millis), JSON.parseObject("{\"@type\":\"java.sql.Time\",\"val\":" + millis + "}", Time.class));
        Assert.assertEquals(new Time(millis), JSON.parseObject("\"" + millis + "\"", Time.class));
        Assert.assertEquals(new Time(millis), JSON.parseObject("\"" + text + "\"", Time.class));
        
        //System.out.println(JSON.toJSONString(new Time(millis), SerializerFeature.WriteClassName));
        
    }

    public static class VO {

        private Time value;

        public Time getValue() {
            return value;
        }

        public void setValue(Time value) {
            this.value = value;
        }

    }
}
