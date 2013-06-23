package com.alibaba.json.bvt.bug;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class Bug_for_xiayucai2012 extends TestCase {
    public void test_for_xiayucai2012() throws Exception {
        String text = "{\"date\":\"0000-00-00 00:00:00\"}";
        JSONObject json = JSON.parseObject(text);
        Date date = json.getObject("date", Date.class);
        
        Assert.assertEquals(new SimpleDateFormat(JSON.DEFFAULT_DATE_FORMAT).parse(json.getString("date")), date);
    }
}
