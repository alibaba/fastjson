package com.alibaba.json.bvt.jdk7;

import java.nio.file.Path;
import java.nio.file.Paths;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class PathTest extends TestCase {
    public void test_for_path() throws Exception {
        Model model = new Model();
        model.path = Paths.get("/root/fastjson");
        
        String text = JSON.toJSONString(model);
        System.out.println(text);
        //windows下，输出为 
        //Assert.assertEquals("{\"path\":\"\\root\\fastjson\"}", text);
        
        //linux ,mac
        //Assert.assertEquals("{\"path\":\"/root/fastjson\"}", text);
        
        Model model2 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.path.toString(), model2.path.toString());
    }
    
    public void test_for_null() throws Exception {
        String text = "{\"path\":null}";
        
        Model model2 = JSON.parseObject(text, Model.class);
        Assert.assertNull(model2.path);
    }
    
    public static class Model {
        public Path path;
    }
}
