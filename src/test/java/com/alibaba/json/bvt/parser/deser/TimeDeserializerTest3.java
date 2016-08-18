package com.alibaba.json.bvt.parser.deser;

import java.sql.Time;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class TimeDeserializerTest3 extends TestCase {
    public void test_time() throws Exception {
        Assert.assertEquals(Time.valueOf("17:00:00"), JSON.parseObject("\"17:00:00\"", Time.class));
    }
    
    public void test_time_null() throws Exception {
        Assert.assertEquals(null, JSON.parseObject("\"\"", Time.class));
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
