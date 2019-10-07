package com.alibaba.json.bvt.issue_2700;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public class Issue2784 extends TestCase {
    public void test_for_issue() throws Exception {
        Model m = new Model();
        m.time = java.time.LocalDateTime.now();
        String str = JSON.toJSONString(m);
        assertEquals("{\"time\":"
                + m.time.atZone(JSON.defaultTimeZone.toZoneId()).toInstant().toEpochMilli()
                + "}", str);
    }

    public void test_for_issue_1() throws Exception {
        Model m = new Model();
        m.ztime = ZonedDateTime.now();
        String str = JSON.toJSONString(m);
        assertEquals("{\"ztime\":"
                + m.ztime.toInstant().toEpochMilli()
                + "}", str);
    }

    public void test_for_issue_2() throws Exception {
        Model m = new Model();
        m.time1 = java.time.LocalDateTime.now();
        String str = JSON.toJSONString(m);
        assertEquals("{\"time1\":"
                + m.time1.atZone(JSON.defaultTimeZone.toZoneId()).toEpochSecond()
                + "}", str);
    }

    public void test_for_issue_3() throws Exception {
        Model m = new Model();
        m.ztime1 = ZonedDateTime.now();
        String str = JSON.toJSONString(m);
        assertEquals("{\"ztime1\":"
                + m.ztime1.toEpochSecond()
                + "}", str);
    }

    public void test_for_issue_4() throws Exception {
        Model m = new Model();
        m.date = new Date();
        String str = JSON.toJSONString(m);
        assertEquals("{\"date\":"
                + m.date.getTime()
                + "}", str);
    }

    public void test_for_issue_5() throws Exception {
        Model m = new Model();
        m.date1 = new Date();
        String str = JSON.toJSONString(m);
        assertEquals("{\"date1\":"
                + (m.date1.getTime() / 1000)
                + "}", str);
    }

    public static class Model {
        @JSONField(format = "millis")
        public LocalDateTime time;

        @JSONField(format = "millis")
        public ZonedDateTime ztime;

        @JSONField(format = "unixtime")
        public LocalDateTime time1;

        @JSONField(format = "unixtime")
        public ZonedDateTime ztime1;

        @JSONField(format = "millis")
        public Date date;

        @JSONField(format = "unixtime")
        public Date date1;
    }
}
