package com.alibaba.json.bvt.serializer.date;

import java.util.Calendar;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class DateTest_ISO8601_UTCTime extends TestCase {

    public void test_date() throws Exception {
        String text = "{\"gmtCreate\":\"2014-10-09T03:07:07.000Z\"}";

        Calendar date = JSON.parseObject(text, VO.class).getGmtCreate();
        Assert.assertNotNull(date);

        Assert.assertEquals(0, date.getTimeZone().getRawOffset() / (3600 * 1000));
    }

    public static class VO {

        private Calendar gmtCreate;

        public Calendar getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(Calendar gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

    }
}
