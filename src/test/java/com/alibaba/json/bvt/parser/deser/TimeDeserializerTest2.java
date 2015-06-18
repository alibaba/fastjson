package com.alibaba.json.bvt.parser.deser;

import java.sql.Time;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class TimeDeserializerTest2 extends TestCase {

    public void test_0() throws Exception {
        long millis = System.currentTimeMillis();
        JSON.parse("{\"@type\":\"java.sql.Time\",\"value\":" + millis + "}");
    }

    public void test_error() throws Exception {
        long millis = System.currentTimeMillis();

        Exception error = null;
        try {
            JSON.parse("{\"@type\":\"java.sql.Time\",33:" + millis + "}");
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parse("{\"@type\":\"java.sql.Time\",\"value\":true}");
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        long millis = System.currentTimeMillis();

        Exception error = null;
        try {
            JSON.parse("{\"@type\":\"java.sql.Time\",\"value\":" + millis + ",}");
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"time\":{}}", VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class VO {

        private Time time;

        public Time getTime() {
            return time;
        }

        public void setTime(Time time) {
            this.time = time;
        }

    }
}
