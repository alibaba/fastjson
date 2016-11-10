package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;

public class FieldDeserializerTest1 extends TestCase {

    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":[-}", Entity.class, 0);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{,,,\"value\":null}", Entity.class, 0);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_2() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":null,\"id\":123}", Entity.class, 0);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_null() throws Exception {
        Entity object = JSON.parseObject("{\"value\":null}", Entity.class, 0);
        Assert.assertNull(object.getValue());
    }
    
    public void test_null_2() throws Exception {
        Entity object = JSON.parseObject("{\"value\":null,\"id\":123}", Entity.class, 0, Feature.IgnoreNotMatch);
        Assert.assertNull(object.getValue());
    }
    
    private static class Entity {

        private V1 value;

        public V1 getValue() {
            return value;
        }

        public void setValue(V1 value) {
            this.value = value;
        }

    }

    private static class V1 {

    }
}
