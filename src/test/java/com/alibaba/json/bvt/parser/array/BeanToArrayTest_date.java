package com.alibaba.json.bvt.parser.array;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class BeanToArrayTest_date extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_date() throws Exception {
        long time = System.currentTimeMillis();
        Model model = JSON.parseObject("[" + time + "," + time + "]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(time, model.v1.getTime());
        Assert.assertEquals(time, model.v2.getTime());
    }
    
    public void test_date_null() throws Exception {
        Model model = JSON.parseObject("[null,null]", Model.class, Feature.SupportArrayToBean);
        Assert.assertNull(model.v1);
        Assert.assertNull(model.v2);
    }
    
    public void test_date2() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", JSON.defaultLocale);
        dateFormat.setTimeZone(JSON.defaultTimeZone);
        Model model = JSON.parseObject("[\"2016-01-01\",\"2016-01-02\"]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(dateFormat.parse("2016-01-01").getTime(), model.v1.getTime());
        Assert.assertEquals(dateFormat.parse("2016-01-02").getTime(), model.v2.getTime());
    }

    public static class Model {
        public Date v1;
        public Date v2;
    }
}
