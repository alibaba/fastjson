package com.alibaba.json.bvt.parser;

import java.util.HashMap;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class CreateInstanceErrorTest2 extends TestCase {

    public void test_ordered_field() throws Exception {
        Exception error = null;
        try {
            Model model = JSON.parseObject("{\"value\":{\"@type\":\"com.alibaba.json.bvt.parser.CreateInstanceErrorTest2$MyMap\"}}", Model.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Model {

        public Object value;
    }
    
    public static class MyMap extends HashMap {
        public MyMap(){
            throw new UnsupportedOperationException();
        }
    }
}
