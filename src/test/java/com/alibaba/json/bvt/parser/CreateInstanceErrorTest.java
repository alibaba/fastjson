package com.alibaba.json.bvt.parser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class CreateInstanceErrorTest extends TestCase {

    public void test_ordered_field() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"id\":1001}", Model.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Model {

        public Model(){
            throw new UnsupportedOperationException();
        }

        public int    id;
        public String name;
    }
}
