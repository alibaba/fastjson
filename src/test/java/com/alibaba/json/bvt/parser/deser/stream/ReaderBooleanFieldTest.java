package com.alibaba.json.bvt.parser.deser.stream;

import java.io.StringReader;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class ReaderBooleanFieldTest extends TestCase {

    public void test_bool_error_0() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"value\":t}"));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_bool_error_1() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"value\":tr}"));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_bool_error_2() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"value\":tru}"));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_bool_error_f0() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"value\":f}"));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_bool_error_f1() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"value\":fa}"));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_bool_error_f2() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"value\":fal}"));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_bool_error_f3() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"value\":fals}"));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_bool_normal() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"value\":false,\"value2\":true}"));
        Model model = reader.readObject(Model.class);
        Assert.assertFalse(model.value);
        Assert.assertTrue(model.value2);
        reader.close();
    }
    
    public void test_bool_normal_2() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"model\":{\"value\":false,\"value2\":true}}"));
        Map<String, Model> map = reader.readObject(new TypeReference<Map<String, Model>>() {});
        Model model = map.get("model");
        Assert.assertFalse(model.value);
        Assert.assertTrue(model.value2);
        reader.close();
    }
    
    public void test_bool_error_map() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"model\":{\"value\":false,\"value2\":true}["));
            reader.readObject(new TypeReference<Map<String, Model>>() {});
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    private static class Model {

        public boolean value;
        public boolean value2;
    }
}
