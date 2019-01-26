package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.joda.time.LocalDateTime;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Issue2241 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_for_issue() throws Exception {
        String text = "{\"createTime\":1548166745}";

        Order o = JSON.parseObject(text, Order.class);
        assertEquals(1548166745000L, o.createTime.getTime());

        String json = JSON.toJSONString(o);
        assertEquals("{\"createTime\":1548166745}", json);
    }

    public void test_for_issue2() throws Exception {
        String text = "{\"createTime\":1548166745}";

        Order2 o = JSON.parseObject(text, Order2.class);
        assertEquals(1548166745000L, o.createTime.getTimeInMillis());

        String json = JSON.toJSONString(o);
        assertEquals("{\"createTime\":1548166745}", json);
    }

    public void test_for_issue3() throws Exception {
        String text = "{\"createTime\":\"20180714224948\"}";

        Order3 o = JSON.parseObject(text, Order3.class);
        assertEquals(1531579788000L, o.createTime.getTimeInMillis());

        String json = JSON.toJSONString(o);
        assertEquals("{\"createTime\":\"20180714224948\"}", json);
    }

    public void test_for_issue4() throws Exception {
        String text = "{\"createTime\":1548166745}";

        Order4 o = JSON.parseObject(text, Order4.class);
        assertEquals(1548166745L, o.createTime.toEpochSecond());

        String json = JSON.toJSONString(o);
        assertEquals("{\"createTime\":1548166745}", json);
    }

    public static class Order {
        @JSONField(format = "unixtime")
        public Date createTime;
    }

    public static class Order2 {
        @JSONField(format = "unixtime")
        public Calendar createTime;
    }

    public static class Order3 {
        @JSONField(format = "yyyyMMddHHmmss")
        public Calendar createTime;
    }

    public static class Order4 {
        @JSONField(format = "unixtime")
        public ZonedDateTime createTime;
    }

}
