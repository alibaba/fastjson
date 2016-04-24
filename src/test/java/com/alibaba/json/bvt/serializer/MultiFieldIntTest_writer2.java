package com.alibaba.json.bvt.serializer;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class MultiFieldIntTest_writer2 extends TestCase {
    
    public void test_for_big_writer() throws Exception {
        List<Model> list = new ArrayList<Model>();
        
        for (int i = 0; i < 1024 * 10; ++i) {
            Model model = new Model();
            model.i = 0;
            model.j = 1;
            model.k = 2;
            model.v = 3;
            model.l = 4;
            model.m = 5;
            model.n = 6;
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
            Assert.assertEquals(list.get(i).i, results.get(i).i);
            Assert.assertEquals(list.get(i).j, results.get(i).j);
            Assert.assertEquals(list.get(i).k, results.get(i).k);
            Assert.assertEquals(list.get(i).v, results.get(i).v);
            Assert.assertEquals(list.get(i).l, results.get(i).l);
            Assert.assertEquals(list.get(i).m, results.get(i).m);
            Assert.assertEquals(list.get(i).n, results.get(i).n);
        }
    }
    
    public static class Model {
        public int i;
        public int j;
        public int k;
        public int l;
        public int m;
        public int n;
        public int v;
    }
}
