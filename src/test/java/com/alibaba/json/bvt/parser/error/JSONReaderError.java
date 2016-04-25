package com.alibaba.json.bvt.parser.error;

import java.io.StringReader;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class JSONReaderError extends TestCase {

    public void test_reader_error() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"id\":"));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_reader_error_1() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"id\":\"aa"));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_reader_no_error() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"id\":\"aa\",\"name\":\"wenshao\"}"));
        Model model = reader.readObject(Model.class);
        Assert.assertEquals("aa", model.id);
        Assert.assertEquals("wenshao", model.name);
        reader.close();
    }
    
    public void test_reader_no_error_1() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"model\":{\"id\":\"aa\",\"name\":\"wenshao\"}}"));
        Map<String, Model> map = reader.readObject(new TypeReference<Map<String, Model>>() {});
        Model model = map.get("model");
        Assert.assertEquals("aa", model.id);
        Assert.assertEquals("wenshao", model.name);
        reader.close();
    }
    
    public void test_reader_no_error_2() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"model\":{\"id\":\"aa\",\"name\":\"wenshao\"},\"model2\":{\"id\":\"bb\",\"name\":\"ljw\"}}"));
        Map<String, Model> map = reader.readObject(new TypeReference<Map<String, Model>>() {});
        
        {
            Model model = map.get("model");
            Assert.assertEquals("aa", model.id);
            Assert.assertEquals("wenshao", model.name);
        }
        {
            Model model = map.get("model2");
            Assert.assertEquals("bb", model.id);
            Assert.assertEquals("ljw", model.name);
        }
        reader.close();
    }
    
    public void test_reader_error_3() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"model\":{\"id\":\"aa\",\"name\":\"wenshao\"}["));
            Map<String, Model> map = reader.readObject(new TypeReference<Map<String, Model>>() {});
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Model {

        public String id;
        public String name;
    }
}
