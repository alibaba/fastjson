package com.alibaba.json.bvt.writeAsArray;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class WriteAsArray_string extends TestCase {

    public void test_0() throws Exception {
        Model model = new Model();

        String text = JSON.toJSONString(model, SerializerFeature.BeanToArray);
        Assert.assertEquals("[null]", text);

        Model model2 = JSON.parseObject(text, Model.class, Feature.SupportArrayToBean);
        Assert.assertNull(model2.name);
    }

    public void test_1() throws Exception {
        Model model = new Model();
        model.name = "abc";
        String text = JSON.toJSONString(model, SerializerFeature.BeanToArray);
        Assert.assertEquals("[\"abc\"]", text);

        Model model2 = JSON.parseObject(text, Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(model.name, model2.name);
    }
    
    public void test_error_0() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[n", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[nu", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[nul", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[null", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_4() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[\"ab", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_5() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[\"ab\"", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Model {

        public String name;

    }
}
