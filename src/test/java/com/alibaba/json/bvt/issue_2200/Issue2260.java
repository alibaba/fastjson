package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;

public class Issue2260 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\"date\":\"1950-07-14\"}";
        M1 m = JSON.parseObject(json, M1.class);
        assertEquals(1950, m.date.get(Calendar.YEAR));
    }

    public void test_for_jdk8_zdt_1() throws Exception {
        String json = "{\"date\":\"1950-07-14\"}";
        M2 m = JSON.parseObject(json, M2.class);
        assertEquals(1950, m.date.getYear());
    }

    public void test_for_jdk8_zdt_2() throws Exception {
        String json = "{\"date\":\"1950-07-14 12:23:34\"}";
        M2 m = JSON.parseObject(json, M2.class);
        assertEquals(1950, m.date.getYear());
    }

    public void test_for_jdk8_zdt_3() throws Exception {
        String json = "{\"date\":\"1950-07-14T12:23:34\"}";
        M2 m = JSON.parseObject(json, M2.class);
        assertEquals(1950, m.date.getYear());
    }

    public void test_for_jdk8_ldt_1() throws Exception {
        String json = "{\"date\":\"1950-07-14\"}";
        M3 m = JSON.parseObject(json, M3.class);
        assertEquals(1950, m.date.getYear());
    }

    public void test_for_jdk8_ldt_2() throws Exception {
        String json = "{\"date\":\"1950-07-14 12:23:34\"}";
        M3 m = JSON.parseObject(json, M3.class);
        assertEquals(1950, m.date.getYear());
    }

    public void test_for_jdk8_ldt_3() throws Exception {
        String json = "{\"date\":\"1950-07-14T12:23:34\"}";
        M3 m = JSON.parseObject(json, M3.class);
        assertEquals(1950, m.date.getYear());
    }

    public static class M1 {
        public Calendar date;
    }

    public static class M2 {
        public ZonedDateTime date;
    }

    public static class M3 {
        public LocalDateTime date;
    }

}
