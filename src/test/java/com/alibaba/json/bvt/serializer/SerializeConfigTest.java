package com.alibaba.json.bvt.serializer;

import java.util.LinkedHashMap;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeConfigTest extends TestCase {

    public void test_0() throws Exception {
        SerializeConfig config = new SerializeConfig();

        Exception error = null;
        try {
            config.createJavaBeanSerializer(int.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_1() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.setTypeKey("%type");
        Assert.assertEquals("%type", config.getTypeKey());

        Assert.assertEquals("{\"@type\":\"java.util.LinkedHashMap\"}",
                            JSON.toJSONString(new LinkedHashMap(), config, SerializerFeature.WriteClassName));
    }
}
