package com.alibaba.json.bvt.parser.deser.list;

import java.util.Map;
import java.util.TreeSet;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class ListStringFieldTest_dom_treeSet extends TestCase {

    public void test_list() throws Exception {
        String text = "{\"values\":[\"a\",\"b\",\"ab\\\\c\"]}";

        Model model = JSON.parseObject(text, Model.class);

        Assert.assertEquals(3, model.values.size());
        Assert.assertTrue(model.values.contains("a"));
        Assert.assertTrue(model.values.contains("b"));
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

        private TreeSet<String> values;

        public TreeSet<String> getValues() {
            return values;
        }

        public void setValues(TreeSet<String> values) {
            this.values = values;
        }

    }
}
