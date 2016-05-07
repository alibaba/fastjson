package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import junit.framework.TestCase;

public class ConstructorErrorTest extends TestCase {

    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Model {

        public Model(){
            throw new IllegalStateException();
        }
    }
}
