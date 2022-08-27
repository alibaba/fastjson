package com.alibaba.json.bvt.parser.deser.list;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.json.bvt.parser.deser.list.ListStringFieldTest_dom_array.Model;

import junit.framework.TestCase;

public class ListStringFieldTest_dom_array_2 extends TestCase {

    public void test_list() throws Exception {
        String text = "[[\"a\",null,\"b\",\"ab\\\\c\"],[]]";

        Model model = JSON.parseObject(text, Model.class);
        Assert.assertEquals(4, model.values.size());
        Assert.assertEquals("a", model.values.get(0));
        Assert.assertEquals(null, model.values.get(1));
        Assert.assertEquals("b", model.values.get(2));
        Assert.assertEquals("ab\\c", model.values.get(3));

        Assert.assertEquals(0, model.values2.size());
    }

    public void test_list2() throws Exception {
        String text = "{\"values\":[\"a\",null,\"b\",\"ab\\\\c\"],\"values2\":[]}";

        Model model = JSON.parseObject(text, Model.class);
        Assert.assertEquals(4, model.values.size());
        Assert.assertEquals("a", model.values.get(0));
        Assert.assertEquals(null, model.values.get(1));
        Assert.assertEquals("b", model.values.get(2));
        Assert.assertEquals("ab\\c", model.values.get(3));

        Assert.assertEquals(0, model.values2.size());
    }

    @JSONType(parseFeatures = Feature.SupportArrayToBean)
    public static class Model {

        public List<String> values;
        public List<String> values2;

    }
}
