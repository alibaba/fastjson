package com.alibaba.json.bvt;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class StringFieldTest_special_reader extends TestCase {
    public void test_special() throws Exception {
        Model model = new Model();
        model.name = "a\\bc";
        String text = JSON.toJSONString(model);
        Assert.assertEquals("{\"name\":\"a\\\\bc\"}", text);

        Model model2 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.name, model2.name);
    }
    
    public void test_special_2() throws Exception {
        Model model = new Model();
        model.name = "a\\bc\"";
        String text = JSON.toJSONString(model);
        Assert.assertEquals("{\"name\":\"a\\\\bc\\\"\"}", text);

        Model model2 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.name, model2.name);
    }

    public static class Model {

        public String name;

    }
}
