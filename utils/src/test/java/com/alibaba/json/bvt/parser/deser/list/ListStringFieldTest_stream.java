package com.alibaba.json.bvt.parser.deser.list;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class ListStringFieldTest_stream extends TestCase {

    public void test_list() throws Exception {
        String text = "{\"values\":[\"a\",null,\"b\",\"ab\\\\c\\\"\"]}";

        JSONReader reader = new JSONReader(new StringReader(text));
        Model model = reader.readObject(Model.class);
        Assert.assertEquals(4, model.values.size());
        Assert.assertEquals("a", model.values.get(0));
        Assert.assertEquals(null, model.values.get(1));
        Assert.assertEquals("b", model.values.get(2));
        Assert.assertEquals("ab\\c\"", model.values.get(3));
    }

    public void test_null() throws Exception {
        String text = "{\"values\":null}";
        JSONReader reader = new JSONReader(new StringReader(text));
        Model model = reader.readObject(Model.class);
        Assert.assertNull(model.values);
    }

    public void test_empty() throws Exception {
        String text = "{\"values\":[]}";
        JSONReader reader = new JSONReader(new StringReader(text));
        Model model = reader.readObject(Model.class);
        Assert.assertEquals(0, model.values.size());
    }

    public void test_map_empty() throws Exception {
        String text = "{\"model\":{\"values\":[]}}";
        JSONReader reader = new JSONReader(new StringReader(text));
        Map<String, Model> map = reader.readObject(new TypeReference<Map<String, Model>>() {
        });
        Model model = (Model) map.get("model");
        Assert.assertEquals(0, model.values.size());
    }

    public void test_notMatch() throws Exception {
        String text = "{\"value\":[]}";
        JSONReader reader = new JSONReader(new StringReader(text));
        Model model = reader.readObject(Model.class);
        Assert.assertNull(model.values);
    }

    public void test_error() throws Exception {
        String text = "{\"values\":[1";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_1() throws Exception {
        String text = "{\"values\":[\"b\"[";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_2() throws Exception {
        String text = "{\"model\":{\"values\":[][";
        JSONReader reader = new JSONReader(new StringReader(text));
        

        Exception error = null;
        try {
            reader.readObject(new TypeReference<Map<String, Model>>() {
            });
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_3() throws Exception {
        String text = "{\"model\":{\"values\":[]}[";
        JSONReader reader = new JSONReader(new StringReader(text));
        

        Exception error = null;
        try {
            reader.readObject(new TypeReference<Map<String, Model>>() {
            });
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_4() throws Exception {
        String text = "{\"model\":{\"values\":[\"aaa]}[";
        JSONReader reader = new JSONReader(new StringReader(text));
        

        Exception error = null;
        try {
            reader.readObject(new TypeReference<Map<String, Model>>() {
            });
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_n() throws Exception {
        String text = "{\"values\":[n";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_nu() throws Exception {
        String text = "{\"values\":[nu";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_nul() throws Exception {
        String text = "{\"values\":[nul";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_null() throws Exception {
        String text = "{\"values\":[null";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_rbacket() throws Exception {
        String text = "{\"values\":[null,]";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Model {

        private List<String> values;

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }

    }
}
