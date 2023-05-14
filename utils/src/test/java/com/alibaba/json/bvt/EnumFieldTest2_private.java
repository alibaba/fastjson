package com.alibaba.json.bvt;

import java.io.StringReader;

import org.junit.Assert;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class EnumFieldTest2_private extends TestCase {

    public void test_0() throws Exception {
        JSONReader read = new JSONReader(new StringReader("[1,2]"));
        read.config(Feature.SupportArrayToBean, true);
        Model model = read.readObject(Model.class);
        Assert.assertEquals(Type.B, model.value);
        Assert.assertEquals(Type.C, model.value1);
        read.close();
    }
    
    public void test_1() throws Exception {
        JSONReader read = new JSONReader(new StringReader("[\"A\",\"B\"]"));
        read.config(Feature.SupportArrayToBean, true);
        Model model = read.readObject(Model.class);
        Assert.assertEquals(Type.A, model.value);
        Assert.assertEquals(Type.B, model.value1);
        read.close();
    }
    
    public void test_2() throws Exception {
        JSONReader read = new JSONReader(new StringReader("[null,null]"));
        read.config(Feature.SupportArrayToBean, true);
        Model model = read.readObject(Model.class);
        Assert.assertEquals(null, model.value);
        Assert.assertEquals(null, model.value1);
        read.close();
    }
    
    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            JSONReader read = new JSONReader(new StringReader("[null:null]"));
            read.config(Feature.SupportArrayToBean, true);
            Model model = read.readObject(Model.class);
            read.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_n() throws Exception {
        Exception error = null;
        try {
            JSONReader read = new JSONReader(new StringReader("[n"));
            read.config(Feature.SupportArrayToBean, true);
            Model model = read.readObject(Model.class);
            read.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_nu() throws Exception {
        Exception error = null;
        try {
            JSONReader read = new JSONReader(new StringReader("[nu"));
            read.config(Feature.SupportArrayToBean, true);
            Model model = read.readObject(Model.class);
            read.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_nul() throws Exception {
        Exception error = null;
        try {
            JSONReader read = new JSONReader(new StringReader("[nul"));
            read.config(Feature.SupportArrayToBean, true);
            Model model = read.readObject(Model.class);
            read.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    private static class Model {

        public Type value;
        public Type value1;

    }

    public static enum Type {
                             A, B, C
    }
}
