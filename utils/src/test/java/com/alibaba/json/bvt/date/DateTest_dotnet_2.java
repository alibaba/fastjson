package com.alibaba.json.bvt.date;

import java.util.Date;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class DateTest_dotnet_2 extends TestCase {

    public void test_date() throws Exception {
        String text = "{\"date\":\"/Date(1461081600000+0500)/\"}";

        Model model = JSON.parseObject(text, Model.class);
        Assert.assertEquals(1461081600000L, model.date.getTime());
    }

    public static class Model {

        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

    }
}
