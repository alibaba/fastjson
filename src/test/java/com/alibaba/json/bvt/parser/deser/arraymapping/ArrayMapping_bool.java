package com.alibaba.json.bvt.parser.deser.arraymapping;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class ArrayMapping_bool extends TestCase {
    public void test_for_true() throws Exception {
        Model model = JSON.parseObject("[true,\"wenshao\"]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(true, model.id);
        Assert.assertEquals("wenshao", model.name);
    }
    
    public void test_for_false() throws Exception {
        Model model = JSON.parseObject("[false,\"wenshao\"]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(false, model.id);
        Assert.assertEquals("wenshao", model.name);
    }
    
    public static class Model {
        public boolean id;
        public String name;
        
    }
}
