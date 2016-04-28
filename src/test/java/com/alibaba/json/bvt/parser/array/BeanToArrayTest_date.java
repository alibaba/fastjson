package com.alibaba.json.bvt.parser.array;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class BeanToArrayTest_date extends TestCase {

    public void test_date() throws Exception {
        long time = System.currentTimeMillis();
        Model model = JSON.parseObject("[" + time + "]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(time, model.v1.getTime());
    }
    
    public void test_date_null() throws Exception {
        Model model = JSON.parseObject("[null]", Model.class, Feature.SupportArrayToBean);
        Assert.assertNull(model.v1);
    }
    
    public void test_date2() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Model model = JSON.parseObject("[\"2016-01-01\"]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(dateFormat.parse("2016-01-01").getTime(), model.v1.getTime());
    }

    public static class Model {
        public Date v1;
    }
}
