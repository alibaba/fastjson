package com.alibaba.json.bvt;

import java.io.StringReader;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class EnumFieldTest extends TestCase {

    public void test_special() throws Exception {
        JSONReader read = new JSONReader(new StringReader("{\"value\":1}"));
        Model model = read.readObject(Model.class);
        Assert.assertEquals(Type.B, model.value);
        read.close();
    }
    
    public void test_1() throws Exception {
        JSONReader read = new JSONReader(new StringReader("{\"value\":\"A\",\"value1\":\"B\"}"));
        Model model = read.readObject(Model.class);
        Assert.assertEquals(Type.A, model.value);
        Assert.assertEquals(Type.B, model.value1);
        read.close();
    }
    
    public void test_map() throws Exception {
        JSONReader read = new JSONReader(new StringReader("{\"model\":{\"value\":\"A\",\"value1\":\"B\"}}"));
        Map<String, Model> map = read.readObject(new TypeReference<Map<String, Model>>(){});
        Model model = (Model) map.get("model");
        Assert.assertEquals(Type.A, model.value);
        Assert.assertEquals(Type.B, model.value1);
        read.close();
    }

    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSONReader read = new JSONReader(new StringReader("{\"value\":\"a\\b\"}"));
            read.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            JSONReader read = new JSONReader(new StringReader("{\"value\":\"A\",\"value1\":\"B\"["));
            Model model = read.readObject(Model.class);
            read.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_2() throws Exception {
        Exception error = null;
        try {
            JSONReader read = new JSONReader(new StringReader("{\"model\":{\"value\":\"A\",\"value1\":\"B\"}["));
            Map<String, Model> map = read.readObject(new TypeReference<Map<String, Model>>(){});
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
