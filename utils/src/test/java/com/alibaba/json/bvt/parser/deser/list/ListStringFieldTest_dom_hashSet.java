package com.alibaba.json.bvt.parser.deser.list;

import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.json.bvt.parser.deser.list.ListStringFieldTest_dom_array.Model;

import junit.framework.TestCase;

public class ListStringFieldTest_dom_hashSet extends TestCase {

    public void test_list() throws Exception {
        String text = "{\"values\":[\"a\",null,\"b\",\"ab\\\\c\"]}";

        Model model = JSON.parseObject(text, Model.class);

        Assert.assertEquals(4, model.values.size());
        Assert.assertTrue(model.values.contains("a"));
        Assert.assertTrue(model.values.contains("b"));
        Assert.assertTrue(model.values.contains(null));
        Assert.assertTrue(model.values.contains("ab\\c"));
    }

    public void test_null() throws Exception {
        String text = "{\"values\":null}";
        Model model = JSON.parseObject(text, Model.class);
        Assert.assertNull(model.values);
    }

    public void test_empty() throws Exception {
        String text = "{\"values\":[]}";
        Model model = JSON.parseObject(text, Model.class);
        Assert.assertEquals(0, model.values.size());
    }

    public void test_map_empty() throws Exception {
        String text = "{\"model\":{\"values\":[]}}";
        Map<String, Model> map = JSON.parseObject(text, new TypeReference<Map<String, Model>>() {
        });
        Model model = (Model) map.get("model");
        Assert.assertEquals(0, model.values.size());
    }

    public void test_notMatch() throws Exception {
        String text = "{\"value\":[]}";
        Model model = JSON.parseObject(text, Model.class);
        Assert.assertNull(model.values);
    }

    public void test_error() throws Exception {
        String text = "{\"values\":[1";
        Exception error = null;
        try {
            Model model = JSON.parseObject(text, Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        String text = "{\"values\":[\"b\"[";
        Exception error = null;
        try {
            Model model = JSON.parseObject(text, Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        String text = "{\"model\":{\"values\":[][";

        Exception error = null;
        try {
            JSON.parseObject(text, new TypeReference<Map<String, Model>>() {
            });
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() throws Exception {
        String text = "{\"model\":{\"values\":[]}[";

        Exception error = null;
        try {
            JSON.parseObject(text, new TypeReference<Map<String, Model>>() {
            });
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Model {

        private Set<String> values;

        public Set<String> getValues() {
            return values;
        }

        public void setValues(Set<String> values) {
            this.values = values;
        }

    }
}
