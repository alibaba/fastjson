package com.alibaba.json.bvt;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class StringFieldTest_special_2 extends TestCase {
    public void test_special() throws Exception {
        Model model = new Model();
        StringBuilder buf = new StringBuilder();
        for (int i = Character.MIN_VALUE; i < Character.MAX_VALUE; ++i) {
            buf.append((char) i);
        }
        model.name = buf.toString();
        
        String text = JSON.toJSONString(model);

        Model model2 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.name, model2.name);
    }
    
    public void test_special_browsecue() throws Exception {
        Model model = new Model();
        StringBuilder buf = new StringBuilder();
        for (int i = Character.MIN_VALUE; i < Character.MAX_VALUE; ++i) {
            buf.append((char) i);
        }
        model.name = buf.toString();
        
        String text = JSON.toJSONString(model, SerializerFeature.BrowserSecure);
        text = text.replaceAll("&lt;", "<");
        text = text.replaceAll("&gt;", ">");
//        text = text.replaceAll("\\\\/", "/");
        Model model2 = JSON.parseObject(text, Model.class);

        for (int i = 0; i < model.name.length() && i < model2.name.length(); ++i) {
            char c1 = model.name.charAt(i);
            char c2 = model.name.charAt(i);
            if (c1 != c2) {
                System.out.println("diff : " + c1 + " -> " + c2);
                break;
            }
        }
//        String str = model2.name.substring(65535);
//        System.out.println(str);
        Assert.assertEquals(model.name.length(), model2.name.length());
        Assert.assertEquals(model.name, model2.name);
    }
    
    public void test_special_browsecompatible() throws Exception {
        Model model = new Model();
        StringBuilder buf = new StringBuilder();
        for (int i = Character.MIN_VALUE; i < Character.MAX_VALUE; ++i) {
            buf.append((char) i);
        }
        model.name = buf.toString();
        
        String text = JSON.toJSONString(model, SerializerFeature.BrowserCompatible);

        Model model2 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.name, model2.name);
    }
    
    private static class Model {

        public String name;

    }
}
