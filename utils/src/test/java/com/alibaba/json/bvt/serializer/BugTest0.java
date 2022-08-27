package com.alibaba.json.bvt.serializer;

import java.sql.Date;
import java.sql.Timestamp;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class BugTest0 extends TestCase {

    public void test_0() throws Exception {
        Timestamp t = new Timestamp(System.currentTimeMillis());

        String text = JSON.toJSONString(t);

        Timestamp t1 = JSON.parseObject(text, Timestamp.class);
        Assert.assertEquals(t, t1);
    }

    public void test_1() throws Exception {
        long t1 = System.currentTimeMillis();
        String text = JSON.toJSONString(t1);

        Timestamp t2 = JSON.parseObject(text, Timestamp.class);
        Assert.assertEquals(t1, t2.getTime());
    }

    public void test_2() throws Exception {
        Date t = new Date(System.currentTimeMillis());

        String text = JSON.toJSONString(t);

        Date t1 = JSON.parseObject(text, Date.class);
        Assert.assertEquals(t, t1);
    }

    public void test_3() throws Exception {
        long t1 = System.currentTimeMillis();
        String text = JSON.toJSONString(t1);

        Date t2 = JSON.parseObject(text, Date.class);
        Assert.assertEquals(t1, t2.getTime());
    }

    public void test_4() throws Exception {
        A a = new A();
        a.setDate(new java.sql.Date(System.currentTimeMillis()));
        a.setTime(new java.sql.Timestamp(System.currentTimeMillis()));
        String text = JSON.toJSONString(a);

        A a1 = JSON.parseObject(text, A.class);

        Assert.assertEquals(a.getDate(), a1.getDate());
        Assert.assertEquals(a.getTime(), a1.getTime());
    }

    public void test_error_0() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("\"222A\"", Timestamp.class);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("\"222B\"", Date.class);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("true", Timestamp.class);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_4() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("true", Date.class);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public static class A {

        private java.sql.Timestamp time;
        private java.sql.Date      date;

        public java.sql.Timestamp getTime() {
            return time;
        }

        public void setTime(java.sql.Timestamp time) {
            this.time = time;
        }

        public java.sql.Date getDate() {
            return date;
        }

        public void setDate(java.sql.Date date) {
            this.date = date;
        }

    }
}
