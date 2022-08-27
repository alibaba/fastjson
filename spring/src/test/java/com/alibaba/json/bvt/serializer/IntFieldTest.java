package com.alibaba.json.bvt.serializer;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class IntFieldTest extends TestCase {

    public void test_model() throws Exception {
        Model model = new Model();
        model.id = -1001;
        
        String text = JSON.toJSONString(model);
        Assert.assertEquals("{\"id\":-1001}", text);
    }
    
    public void test_model_max() throws Exception {
        Model model = new Model();
        model.id = Integer.MIN_VALUE;
        
        String text = JSON.toJSONString(model);
        Assert.assertEquals("{\"id\":-2147483648}", text);
    }

    public static class Model {

        public int id;
    }
}
