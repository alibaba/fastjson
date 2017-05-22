package com.alibaba.json.bvt.parser.deser.list;

import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class ListStringFieldTest extends TestCase {

    public void test_list() throws Exception {
        String text = "[[\"a\",null,\"b\"]]";
        Model model = JSON.parseObject(text, Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(3, model.values.size());
        Assert.assertEquals("a", model.values.get(0));
        Assert.assertEquals(null, model.values.get(1));
        Assert.assertEquals("b", model.values.get(2));
    }
    
    public void test_null() throws Exception {
        String text = "[null]";
        Model model = JSON.parseObject(text, Model.class, Feature.SupportArrayToBean);
        Assert.assertNull(model.values);
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
