package com.alibaba.json.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class DateDemo extends TestCase { 

    public void test_0() throws Exception {
        Date date = new Date(); 
        String text = JSON.toJSONStringWithDateFormat(date, "yyyy-MM-dd");
        Assert.assertEquals(JSON.toJSONString(new SimpleDateFormat("yyyy-MM-dd").format(date)), text);
    }


}
