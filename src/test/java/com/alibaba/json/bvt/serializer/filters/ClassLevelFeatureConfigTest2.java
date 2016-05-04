package com.alibaba.json.bvt.serializer.filters;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class ClassLevelFeatureConfigTest2 extends TestCase {
    public void test_0() throws Exception {
        SerializeConfig config = new SerializeConfig();
        
        config.config(Model.class, SerializerFeature.BeanToArray, true);
        
        Model model = new Model();
        model.id = 1001;
        
        Assert.assertEquals("[1001]", JSON.toJSONString(model, config));
        
        config.config(Model.class, SerializerFeature.BeanToArray, false);
        Assert.assertEquals("{\"id\":1001}", JSON.toJSONString(model, config));
    }
    
    public static class Model {
        public int id;
    }
}
