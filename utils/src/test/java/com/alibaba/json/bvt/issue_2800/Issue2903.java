package com.alibaba.json.bvt.issue_2800;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue2903 extends TestCase {

    public void test_1() {
        String date1 = "{createTime:\"1570636800000\"}";
        String date2 = "{createTime:1570636800000}";
        LoginRequestDTO dto = JSON.parseObject(date1, LoginRequestDTO.class);
        LoginRequestDTO dto2 = JSON.parseObject(date2, LoginRequestDTO.class);
        assertEquals(dto.createTime, dto2.createTime);
    }

    public void test_2() {
        String date1 = "{createTime:\"1570636800000\"}";
        String date2 = "{createTime:1570636800000}";
        LoginRequestDTO2 dto = JSON.parseObject(date1, LoginRequestDTO2.class);
        LoginRequestDTO2 dto2 = JSON.parseObject(date2, LoginRequestDTO2.class);
        assertEquals(dto.createTime, dto2.createTime);
    }

    public void test_3() {
        String date1 = "{createTime:\"1570636800000\"}";
        String date2 = "{createTime:1570636800000}";
        LoginRequestDTO3 dto = JSON.parseObject(date1, LoginRequestDTO3.class);
        LoginRequestDTO3 dto2 = JSON.parseObject(date2, LoginRequestDTO3.class);
        assertEquals(dto.createTime, dto2.createTime);
    }

    public void test_4() {
        String date1 = "{createTime:\"1570636800000\"}";
        String date2 = "{createTime:1570636800000}";
        LoginRequestDTO4 dto = JSON.parseObject(date1, LoginRequestDTO4.class);
        LoginRequestDTO4 dto2 = JSON.parseObject(date2, LoginRequestDTO4.class);
        assertEquals(dto.createTime, dto2.createTime);
    }

    public void test_5() {
        String date1 = "{createTime:\"1570636800000\"}";
        String date2 = "{createTime:1570636800000}";
        LoginRequestDTO5 dto = JSON.parseObject(date1, LoginRequestDTO5.class);
        LoginRequestDTO5 dto2 = JSON.parseObject(date2, LoginRequestDTO5.class);
        assertEquals(dto.createTime, dto2.createTime);
    }

    public void test_6() {
        String date1 = "{createTime:\"1570636800000\"}";
        String date2 = "{createTime:1570636800000}";
        LoginRequestDTO6 dto = JSON.parseObject(date1, LoginRequestDTO6.class);
        LoginRequestDTO6 dto2 = JSON.parseObject(date2, LoginRequestDTO6.class);
        assertEquals(dto.createTime, dto2.createTime);
    }

    public void test_7() {
        String date1 = "{createTime:\"1570636800000\"}";
        String date2 = "{createTime:1570636800000}";
        LoginRequestDTO7 dto = JSON.parseObject(date1, LoginRequestDTO7.class);
        LoginRequestDTO7 dto2 = JSON.parseObject(date2, LoginRequestDTO7.class);
        assertEquals(dto.createTime, dto2.createTime);
    }

    public void test_8() {
        String date1 = "{createTime:\"1570636800000\"}";
        String date2 = "{createTime:1570636800000}";
        LoginRequestDTO8 dto = JSON.parseObject(date1, LoginRequestDTO8.class);
        LoginRequestDTO8 dto2 = JSON.parseObject(date2, LoginRequestDTO8.class);
        assertEquals(dto.createTime, dto2.createTime);
    }

    public static class LoginRequestDTO {
        public java.time.LocalDateTime createTime;
    }

    public static class LoginRequestDTO2 {
        public java.time.LocalDate createTime;
    }

    public static class LoginRequestDTO3 {
        public java.time.LocalTime createTime;
    }

    public static class LoginRequestDTO4 {
        public java.time.ZonedDateTime createTime;
    }

    public static class LoginRequestDTO5 {
        public org.joda.time.LocalDateTime createTime;
    }

    public static class LoginRequestDTO6 {
        public org.joda.time.LocalDate createTime;
    }

    public static class LoginRequestDTO7 {
        public org.joda.time.Instant createTime;
    }

    public static class LoginRequestDTO8 {
        public java.time.Instant createTime;
    }
}
