package com.alibaba.json.bvt.serializer.features;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.junit.Assert;

public class JSONDirectTest_number extends TestCase {
    public void test_feature() throws Exception {
        Model model = new Model();
        model.id = 1001;
        model.value = "12.34";
        
        String json = JSON.toJSONString(model);
//        System.out.println(json);
        Assert.assertEquals("{\"id\":1001,\"value\":12.34}", json);
    }

    public static class Model {
        public int id;
        @JSONField(jsonDirect=true)
        public String value;
    }
}
