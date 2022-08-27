package com.alibaba.json.bvt.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class BigStringFieldTest extends TestCase {
    public void test_bigFieldString() throws Exception {
        Model model = new Model();
        model.f0 = random(1024);
        model.f1 = random(1024);
        model.f2 = random(1024);
        model.f3 = random(1024);
        model.f4 = random(1024);
        
        String text = JSON.toJSONString(model);
        Model model2 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model2.f0, model.f0);
        Assert.assertEquals(model2.f1, model.f1);
        Assert.assertEquals(model2.f2, model.f2);
        Assert.assertEquals(model2.f3, model.f3);
        Assert.assertEquals(model2.f4, model.f4);
    }
    
    public void test_list() throws Exception {
        List<Model> list = new ArrayList<Model>();
        for (int i = 0; i < 1000; ++i) {
            Model model = new Model();
            model.f0 = random(64);
            model.f1 = random(64);
            model.f2 = random(64);
            model.f3 = random(64);
            model.f4 = random(64);
            list.add(model);
        }
        String text = JSON.toJSONString(list);
        List<Model> list2 = JSON.parseObject(text, new TypeReference<List<Model>>() {});
        Assert.assertEquals(list.size(), list2.size());
        for (int i = 0; i < 1000; ++i) {
            Assert.assertEquals(list.get(i).f0, list2.get(i).f0);    
            Assert.assertEquals(list.get(i).f1, list2.get(i).f1);    
            Assert.assertEquals(list.get(i).f2, list2.get(i).f2);    
            Assert.assertEquals(list.get(i).f3, list2.get(i).f3);    
            Assert.assertEquals(list.get(i).f4, list2.get(i).f4);    
        }
    }
    
    public String random(int count) {
        Random random = new Random();
        
        char[] chars = new char[count];
        for (int i = 0; i < count; ++i) {
            chars[i] = (char) random.nextInt();
        }
        
        return new String(chars);
    }
    
    public static class Model {
        public String f0;
        public String f1;
        public String f2;
        public String f3;
        public String f4;
    }
}
