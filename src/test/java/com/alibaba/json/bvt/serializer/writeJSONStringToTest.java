package com.alibaba.json.bvt.serializer;

import java.io.ByteArrayOutputStream;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class writeJSONStringToTest extends TestCase {
    public void test_writeJSONStringTo() throws Exception {
        Model model = new Model();
        model.id = 1001;
        model.name = "中文名称";
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JSON.writeJSONString(os, model);
        os.close();
        
        byte[] bytes = os.toByteArray();
        String text = new String(bytes, "UTF-8");
        Assert.assertEquals("{\"id\":1001,\"name\":\"中文名称\"}", text);
    }
    
    public static class Model {
        public int id;
        public String name;
    }
}
