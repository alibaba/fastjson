package com.alibaba.json.bvt.writeAsArray;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class WriteAsArray_string_special extends TestCase {

    
    public void test_0() throws Exception {
        Model model = new Model();
        model.name = "a\\bc";
        String text = JSON.toJSONString(model, SerializerFeature.BeanToArray);
        Assert.assertEquals("[\"a\\\\bc\"]", text);

        Model model2 = JSON.parseObject(text, Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(model.name, model2.name);
    }
    
    public void test_1() throws Exception {
        Model model = new Model();
        model.name = "a\\bc\"";
        String text = JSON.toJSONString(model, SerializerFeature.BeanToArray);
        Assert.assertEquals("[\"a\\\\bc\\\"\"]", text);

        Model model2 = JSON.parseObject(text, Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(model.name, model2.name);
    }

    public static class Model {

        public String name;

    }
}
