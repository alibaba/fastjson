package com.alibaba.json.bvt.serializer;

import java.io.StringReader;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;

import junit.framework.TestCase;

public class IntFieldTest2 extends TestCase {

    public void test_model() throws Exception {
        Model model = new Model();
        model.id = -1001;
        model.id2 = -1002;

        String text = JSON.toJSONString(model);
        Assert.assertEquals("{\"id\":-1001,\"id2\":-1002}", text);
    }

    public void test_model_max() throws Exception {
        Model model = new Model();
        model.id = Integer.MIN_VALUE;
        model.id2 = Integer.MAX_VALUE;

        String text = JSON.toJSONString(model);
        Assert.assertEquals("{\"id\":-2147483648,\"id2\":2147483647}", text);
        {
            JSONReader reader = new JSONReader(new StringReader(text));
            Model model2 = reader.readObject(Model.class);
            Assert.assertEquals(model.id, model2.id);
            Assert.assertEquals(model.id2, model2.id2);
            reader.close();
        }
    }

    public static class Model {

        public int id;
        public int id2;
    }
}
