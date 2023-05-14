package com.alibaba.json.bvt.serializer;

import java.util.LinkedHashMap;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class SerializeConfigTest2_private extends TestCase {

    public void test_1() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.setTypeKey("%type");
        Assert.assertEquals("%type", config.getTypeKey());
        
        Model model = new Model();
        model.value = 1001;

        Assert.assertEquals("{\"%type\":\"com.alibaba.json.bvt.serializer.SerializeConfigTest2_private$Model\",\"value\":1001}",
                            JSON.toJSONString(model, config, SerializerFeature.WriteClassName));
    }
    
    private static class Model {
        public int value;
    }
}
