package com.alibaba.json.bvt.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class BigListStringFieldTest_private extends TestCase {
    public void test_list() throws Exception {
        Model model = new Model();
        model.values = new ArrayList<String>(10000);
        for (int i = 0; i < 10000; ++i) {
            String value = random(100);
            model.values.add(value);
        }
        String text = JSON.toJSONString(model);
        Model model2 = JSON.parseObject(text, Model.class);
        
        Assert.assertEquals(model.values, model2.values);
    }
    
    public void test_list_browserComptible() throws Exception {
        Model model = new Model();
        model.values = new ArrayList<String>(10000);
        for (int i = 0; i < 10000; ++i) {
            String value = random(100);
            model.values.add(value);
        }
        String text = JSON.toJSONString(model, SerializerFeature.BrowserCompatible);
        Model model2 = JSON.parseObject(text, Model.class);
        
        Assert.assertEquals(model.values, model2.values);
    }
    
    public void test_list_browserSecure() throws Exception {
        Model model = new Model();
        model.values = new ArrayList<String>(10000);
        for (int i = 0; i < 10000; ++i) {
            String value = random(100);
            model.values.add(value);
        }
        String text = JSON.toJSONString(model, SerializerFeature.BrowserSecure);

        text = text.replaceAll("&lt;", "<");
        text = text.replaceAll("&gt;", ">");
        Model model2 = JSON.parseObject(text, Model.class);
        
        Assert.assertEquals(model.values, model2.values);
    }
    
    private static class Model {
        public List<String> values;
    }
    
    public String random(int count) {
        Random random = new Random();
        
        char[] chars = new char[count];
        for (int i = 0; i < count; ++i) {
            chars[i] = (char) random.nextInt(256);
        }
        
        return new String(chars);
    }
}
