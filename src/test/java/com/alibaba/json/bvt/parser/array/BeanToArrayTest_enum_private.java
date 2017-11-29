package com.alibaba.json.bvt.parser.array;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class BeanToArrayTest_enum_private extends TestCase {

    public void test_enum() throws Exception {
        Model model = JSON.parseObject("[\"A\",\"B\"]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(Type.A, model.v1);
        Assert.assertEquals(Type.B, model.v2);
    }
    
    public void test_enum_space() throws Exception {
        Model model = JSON.parseObject("[\"A\" ,\"B\" ]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(Type.A, model.v1);
        Assert.assertEquals(Type.B, model.v2);
    }

    public void test_enum_num() throws Exception {
        Model model = JSON.parseObject("[1,0]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(Type.B, model.v1);
        Assert.assertEquals(Type.A, model.v2);
    }

    public void test_enum_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[t,0]", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    private static class Model {

        public Type v1;
        public Type v2;
    }
    
    private static enum Type {
        A, B, C
    }
}
