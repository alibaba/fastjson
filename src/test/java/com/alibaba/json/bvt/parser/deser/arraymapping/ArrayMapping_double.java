package com.alibaba.json.bvt.parser.deser.arraymapping;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class ArrayMapping_double extends TestCase {
    public void test_float() throws Exception {
        Model model = JSON.parseObject("[123.45,\"wenshao\"]", Model.class, Feature.SupportArrayToBean);
        Assert.assertTrue(123.45D == model.id);
        Assert.assertEquals("wenshao", model.name);
    }
    
    public static class Model {
        public double id;
        public String name;
        
    }
}
