package com.alibaba.json.bvt.parser.error;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import junit.framework.TestCase;

public class TypeNotMatchError extends TestCase {

    public void test_0() throws Exception {
        JSON.parseObject("{\"value\":{\"@type\":\"com.alibaba.json.bvt.parser.error.TypeNotMatchError$AA\"}}", Model.class);
        
        Exception error = null;
        try {
        JSON.parseObject("{\"value\":{\"@type\":\"com.alibaba.json.bvt.parser.error.TypeNotMatchError$B\"}}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    private static class Model {

        public A value;
    }

    private static class A {

    }

    private static class AA extends A {

    }

    private static class B {

    }
}
