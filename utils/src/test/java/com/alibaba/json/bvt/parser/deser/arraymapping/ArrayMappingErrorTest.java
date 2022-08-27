package com.alibaba.json.bvt.parser.deser.arraymapping;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class ArrayMappingErrorTest extends TestCase {

    public void test_for_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[1001,2002]", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Model {

        public int    id;
        public String name;

    }
}
