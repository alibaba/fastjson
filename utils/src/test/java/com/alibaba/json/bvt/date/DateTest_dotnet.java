package com.alibaba.json.bvt.date;

import java.util.Date;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class DateTest_dotnet extends TestCase {
    public void test_date() throws Exception {
        String text = "{\"date\":\"/Date(1461081600000)/\"}";
        
        Model model = JSON.parseObject(text, Model.class);
        Assert.assertEquals(1461081600000L, model.date.getTime());
    }
    
    public static class Model {
        public Date date;
    }
}
