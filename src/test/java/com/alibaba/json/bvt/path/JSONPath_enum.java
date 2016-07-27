package com.alibaba.json.bvt.path;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_enum extends TestCase {
    
    public void test_name() throws Exception {
        Model model = new Model();
        model.size = Size.Small;
        
        Assert.assertEquals(Size.Small.name(), JSONPath.eval(model, "$.size.name"));
    }
    
    public void test_orginal() throws Exception {
        Model model = new Model();
        model.size = Size.Small;
        
        Assert.assertEquals(Size.Small.ordinal(), JSONPath.eval(model, "$.size.ordinal"));
    }

    public static class Model {
        public Size size;
    }
    
    public static enum Size {
        Big, Median, Small
    }
}
