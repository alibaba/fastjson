package com.alibaba.json.bvt.parser.deser.generic;

import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class ListStrFieldTest extends TestCase {
    public void test_0() throws Exception {
        Model model = JSON.parseObject("{\"values\":null}", Model.class);
        Assert.assertNull(model.values);
    }
    
    public static class Model {
        public List<String> values;
    }
}
