package com.alibaba.json.bvt.serializer;

import java.io.StringReader;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;

import junit.framework.TestCase;

public class BooleanFieldTest2 extends TestCase {

    public void test_true() throws Exception {
        String text = "{\"f001\":1001,\"value\":true}";

        Model model = JSON.parseObject(text, Model.class);
        Assert.assertTrue(model.value);
    }

    public void test_false() throws Exception {
        String text = "{\"f001\":1001,\"value\":false}";

        Model model = JSON.parseObject(text, Model.class);
        Assert.assertFalse(model.value);
    }

    public void test_true_reader() throws Exception {
        String text = "{\"f001\":1001,\"value\":true}";

        JSONReader reader = new JSONReader(new StringReader(text));
        Model model = reader.readObject(Model.class);
        Assert.assertTrue(model.value);
        reader.close();
    }

    public void test_false_reader() throws Exception {
        String text = "{\"f001\":1001,\"value\":false}";

        JSONReader reader = new JSONReader(new StringReader(text));
        Model model = reader.readObject(Model.class);
        Assert.assertFalse(model.value);
        reader.close();
    }
    
    public void test_1() throws Exception {
        String text = "{\"value\":1}";

        Model model = JSON.parseObject(text, Model.class);
        Assert.assertTrue(model.value);
    }
    
    public void test_0() throws Exception {
        String text = "{\"value\":0}";

        Model model = JSON.parseObject(text, Model.class);
        Assert.assertFalse(model.value);
    }
    
    public void test_1_reader() throws Exception {
        String text = "{\"value\":1}";

        JSONReader reader = new JSONReader(new StringReader(text));
        Model model = reader.readObject(Model.class);
        Assert.assertTrue(model.value);
        reader.close();
    }
    
    public void test_0_reader() throws Exception {
        String text = "{\"value\":0}";

        JSONReader reader = new JSONReader(new StringReader(text));
        Model model = reader.readObject(Model.class);
        Assert.assertFalse(model.value);
        reader.close();
    }

    public static class Model {

        public boolean value;
    }
}
