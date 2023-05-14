package com.alibaba.json.bvt.date;

import java.util.Date;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class DateTest_dotnet_5 extends TestCase {

    public void test_date() throws Exception {
        String text = "{\"date\":\"/Date(1461081600321)/\"}";

        JSONObject model = JSON.parseObject(text);
        Assert.assertEquals(1461081600321L, ((java.util.Date) model.getObject("date", java.util.Date.class)).getTime());
    }

    private static class Model {

        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

    }
}
