package com.alibaba.json.bvt.serializer;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class MultiFieldIntTest_writer extends TestCase {
    
    public void test_for_big_writer() throws Exception {
        List<Model> list = new ArrayList<Model>();
        
        for (int i = 0; i < 1024 * 10; ++i) {
            Model model = new Model();
            model.id = 10000000 + i;
            list.add(model);
        }
        
        StringWriter out = new StringWriter();
        JSONWriter writer = new JSONWriter(out);
        writer.writeObject(list);
        writer.close();
        
        String text = out.toString();
        System.out.println(text);
        List<Model> results = JSON.parseObject(text, new TypeReference<List<Model>>() {});
        
        Assert.assertEquals(list.size(), results.size());
        for (int i = 0; i < results.size(); ++i) {
            Assert.assertEquals(list.get(i).id, results.get(i).id);
        }
    }
    
    public static class Model {
        public int id;
    }
}
