package com.alibaba.json.bvt.jdk8;

import java.util.Optional;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class OptionalTest_empty extends TestCase {

    public void test_optional() throws Exception {
        Model model = new Model();
        model.value = Optional.empty();

        String text = JSON.toJSONString(model);

        Assert.assertEquals("{\"value\":null}", text);

        Model model2 = JSON.parseObject(text, Model.class);

        Assert.assertEquals(model2.value, model.value);
    }


    public static class Model {
        public Optional<Value> value;
    }
    
    public static class Value {
        
    }
}
