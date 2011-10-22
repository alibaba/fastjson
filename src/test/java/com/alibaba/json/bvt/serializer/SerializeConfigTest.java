package com.alibaba.json.bvt.serializer;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializeConfig;

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
}
