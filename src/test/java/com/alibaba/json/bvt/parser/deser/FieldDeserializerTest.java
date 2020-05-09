package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class FieldDeserializerTest extends TestCase {

    public void test_deser() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{'value':{}}", Entity.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    private static class Entity {

        private V1 value;

        public V1 getValue() {
            return value;
        }

        public void setValue(V1 value) {
            throw new RuntimeException();
        }

    }

    private static class V1 {

    }
}
