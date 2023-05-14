package com.alibaba.json.bvt.serializer.features;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class JSONDirectTest extends TestCase {
    public void test_feature() throws Exception {
        Model model = new Model();
        model.id = 1001;
        model.value = "{}";
        
        String json = JSON.toJSONString(model);
//        System.out.println(json);
        Assert.assertEquals("{\"id\":1001,\"value\":{}}", json);
    }

    public static class Model {
        public int id;
        @JSONField(jsonDirect=true)
        public String value;
    }
}
