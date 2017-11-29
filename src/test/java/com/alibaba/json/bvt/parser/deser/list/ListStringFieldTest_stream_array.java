package com.alibaba.json.bvt.parser.deser.list;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class ListStringFieldTest_stream_array extends TestCase {

    public void test_list() throws Exception {
        String text = "[[\"a\",null,\"b\",\"ab\\\\c\\\"a\"]]";

        JSONReader reader = new JSONReader(new StringReader(text));
        Model model = reader.readObject(Model.class);
        Assert.assertEquals(4, model.values.size());
        Assert.assertEquals("a", model.values.get(0));
        Assert.assertEquals(null, model.values.get(1));
        Assert.assertEquals("b", model.values.get(2));
        Assert.assertEquals("ab\\c\"a", model.values.get(3));
    }

    public void test_null() throws Exception {
        String text = "[null]";
        JSONReader reader = new JSONReader(new StringReader(text));
        Model model = reader.readObject(Model.class);
        Assert.assertNull(model.values);
    }

    public void test_empty() throws Exception {
        String text = "[[]]}";
        JSONReader reader = new JSONReader(new StringReader(text));
        Model model = reader.readObject(Model.class);
        Assert.assertEquals(0, model.values.size());
    }

    public void test_map_empty() throws Exception {
        String text = "{\"model\":[[]]}";
        JSONReader reader = new JSONReader(new StringReader(text));
        Map<String, Model> map = reader.readObject(new TypeReference<Map<String, Model>>() {
        });
        Model model = (Model) map.get("model");
        Assert.assertEquals(0, model.values.size());
    }
    
    public void test_map_empty_2() throws Exception {
        String text = "{\"model\":[[]],\"model2\":[[]]}";
        JSONReader reader = new JSONReader(new StringReader(text));
        Map<String, Model> map = reader.readObject(new TypeReference<Map<String, Model>>() {
        });
        Model model = (Model) map.get("model");
        Assert.assertEquals(0, model.values.size());
        
        Model model2 = (Model) map.get("model2");
        Assert.assertEquals(0, model2.values.size());
    }

    public void test_error() throws Exception {
        String text = "[[1{1,}";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_0() throws Exception {
        String text = "[{";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_n() throws Exception {
        String text = "[n";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_nu() throws Exception {
        String text = "[nu";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_nul() throws Exception {
        String text = "[nul";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    
    public void test_error_null() throws Exception {
        String text = "[null";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_fn() throws Exception {
        String text = "[[n";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_fnu() throws Exception {
        String text = "[[nu";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_fnul() throws Exception {
        String text = "[[nul";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    
    public void test_error_fnull() throws Exception {
        String text = "[[null";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_notclose() throws Exception {
        String text = "[[\"aaa";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_1() throws Exception {
        String text = "[[\"b\"[[{";
        JSONReader reader = new JSONReader(new StringReader(text));

        Exception error = null;
        try {
            reader.readObject(Model.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_2() throws Exception {
        String text = "{\"model\":[[][";
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
        String text = "{\"model\":[[]}[";
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
    
    @JSONType(parseFeatures = Feature.SupportArrayToBean)
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
