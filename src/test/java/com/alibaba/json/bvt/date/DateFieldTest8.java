package com.alibaba.json.bvt.date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class DateFieldTest8 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_0() throws Exception {
        Entity object = new Entity();
        object.setValue(new Date());
        String text = JSON.toJSONStringWithDateFormat(object, "yyyy");
        SimpleDateFormat format = new SimpleDateFormat("yyyy", JSON.defaultLocale);
        format.setTimeZone(JSON.defaultTimeZone);
        Assert.assertEquals("{\"value\":\"" + format.format(object.getValue()) + "\"}",
                            text);
    }

    public void test_1() throws Exception {
        Entity object = new Entity();
        object.setValue(new Date());
        String text = JSON.toJSONString(object);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", JSON.defaultLocale);
        format.setTimeZone(JSON.defaultTimeZone);
        Assert.assertEquals("{\"value\":\"" + format.format(object.getValue()) + "\"}",
                            text);
    }

    public static class Entity {

        @JSONField(format = "yyyy-MM-dd")
        private Date value;

        public Date getValue() {
            return value;
        }

        public void setValue(Date value) {
            this.value = value;
        }

    }
}
