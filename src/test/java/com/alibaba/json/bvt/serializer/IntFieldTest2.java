package com.alibaba.json.bvt.serializer;

import java.io.StringReader;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class IntFieldTest2 extends TestCase {

    public void test_model() throws Exception {
        Model model = new Model();
        model.id = -1001;
        model.id2 = -1002;

        String text = JSON.toJSONString(model);
        Assert.assertEquals("{\"id\":-1001,\"id2\":-1002}", text);
    }

    public void test_model_max() throws Exception {
        Model model = new Model();
        model.id = Integer.MIN_VALUE;
        model.id2 = Integer.MAX_VALUE;

        String text = JSON.toJSONString(model);
        Assert.assertEquals("{\"id\":-2147483648,\"id2\":2147483647}", text);
        {
            JSONReader reader = new JSONReader(new StringReader(text));
            Model model2 = reader.readObject(Model.class);
            Assert.assertEquals(model.id, model2.id);
            Assert.assertEquals(model.id2, model2.id2);
            reader.close();
        }
    }

    public void test_model_map() throws Exception {
        String text = "{\"model\":{\"id\":-1001,\"id2\":-1002}}";

        JSONReader reader = new JSONReader(new StringReader(text));
        Map<String, Model> map = reader.readObject(new TypeReference<Map<String, Model>>() {
        });
        Model model2 = map.get("model");
        Assert.assertEquals(-1001, model2.id);
        Assert.assertEquals(-1002, model2.id2);
        reader.close();
    }

    public void test_model_map_error() throws Exception {
        String text = "{\"model\":{\"id\":-1001,\"id2\":-1002[";

        Exception error = null;
        JSONReader reader = new JSONReader(new StringReader(text));
        try {
            reader.readObject(new TypeReference<Map<String, Model>>() {
            });
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_model_map_error_2() throws Exception {
        String text = "{\"model\":{\"id\":-1001,\"id2\":-1002}[";

        Exception error = null;
        JSONReader reader = new JSONReader(new StringReader(text));
        try {
            reader.readObject(new TypeReference<Map<String, Model>>() {
            });
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Model {

        public int id;
        public int id2;
    }
}
