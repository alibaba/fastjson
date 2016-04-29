package com.alibaba.json.bvt;

import java.io.StringReader;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;

import junit.framework.TestCase;

public class StringFieldTest_special extends TestCase {
    public void test_special() throws Exception {
        Model model = new Model();
        model.name = "a\\bc";
        String text = JSON.toJSONString(model);
        Assert.assertEquals("{\"name\":\"a\\\\bc\"}", text);

        JSONReader reader = new JSONReader(new StringReader(text));
        Model model2 = reader.readObject(Model.class);
        Assert.assertEquals(model.name, model2.name);
        reader.close();
    }
    
    public void test_special_2() throws Exception {
        Model model = new Model();
        model.name = "a\\bc\"";
        String text = JSON.toJSONString(model);
        Assert.assertEquals("{\"name\":\"a\\\\bc\\\"\"}", text);

        JSONReader reader = new JSONReader(new StringReader(text));
        Model model2 = reader.readObject(Model.class);
        Assert.assertEquals(model.name, model2.name);
        reader.close();
    }

    public static class Model {

        public String name;

    }
}
