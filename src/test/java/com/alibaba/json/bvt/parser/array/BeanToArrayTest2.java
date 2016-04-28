package com.alibaba.json.bvt.parser.array;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class BeanToArrayTest2 extends TestCase {

    public void test_bool() throws Exception {
        Model model = JSON.parseObject("[true,false]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(true, model.v1);
        Assert.assertEquals(false, model.v2);
    }
    
    public void test_bool_space() throws Exception {
        Model model = JSON.parseObject("[true ,false ]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(true, model.v1);
        Assert.assertEquals(false, model.v2);
    }

    public void test_bool_num() throws Exception {
        Model model = JSON.parseObject("[1,0]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(true, model.v1);
        Assert.assertEquals(false, model.v2);
    }

    public void test_bool_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[t,0]", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Model {

        public boolean v1;
        public boolean v2;
    }
}
