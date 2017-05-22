package com.alibaba.json.bvt.bug;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Bug_for_issue_447 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_for_issue() throws Exception {
        Calendar calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
        calendar.setTimeInMillis(1460563200000L);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Foo foo = new Foo();
        foo.setCreateDate(calendar.getTime());
        String date = JSON.toJSONString(foo, SerializerFeature.UseISO8601DateFormat);
        Assert.assertEquals("{\"createDate\":\"2016-04-14+08:00\"}", date);
        Foo foo2 = JSON.parseObject(date, Foo.class, Feature.AllowISO8601DateFormat);
        Assert.assertEquals("{\"createDate\":\"2016-04-14 00:00:00\"}", JSON.toJSONString(foo2, SerializerFeature.WriteDateUseDateFormat));
    }

    public static class Foo {

        private String name;
        private Date   createDate;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }

    }
}
