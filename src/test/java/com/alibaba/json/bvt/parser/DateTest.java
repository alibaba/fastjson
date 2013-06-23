package com.alibaba.json.bvt.parser;

import java.util.Date;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class DateTest extends TestCase {

    public void test_0() throws Exception {
        Assert.assertNull(JSON.parseObject("", java.sql.Date.class));
        Assert.assertNull(JSON.parseObject(null, java.sql.Date.class));
        Assert.assertNull(JSON.parseObject("null", java.sql.Date.class));
        Assert.assertNull(JSON.parseObject("\"\"", java.sql.Date.class));

        Assert.assertNull(JSON.parseObject("", java.util.Date.class));
        Assert.assertNull(JSON.parseObject(null, java.util.Date.class));
        Assert.assertNull(JSON.parseObject("null", java.util.Date.class));
        Assert.assertNull(JSON.parseObject("\"\"", java.util.Date.class));

        Assert.assertNull(JSON.parseObject("", java.sql.Timestamp.class));
        Assert.assertNull(JSON.parseObject(null, java.sql.Timestamp.class));
        Assert.assertNull(JSON.parseObject("null", java.sql.Timestamp.class));
        Assert.assertNull(JSON.parseObject("\"\"", java.sql.Timestamp.class));
        
        Assert.assertNull(JSON.parseObject("{date:\"\"}", Entity.class).getDate());
    }

    public static class Entity {

        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

    }
}
