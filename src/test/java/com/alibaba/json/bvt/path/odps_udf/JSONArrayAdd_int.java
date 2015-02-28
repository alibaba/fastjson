package com.alibaba.json.bvt.path.odps_udf;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.support.odps.udf.JSONArrayAdd;


public class JSONArrayAdd_int extends TestCase {
    public void test_udf() throws Exception {
        JSONArrayAdd udf = new JSONArrayAdd();
        
        String text = udf.evaluate("[]", "$", new Long(1), new Long(2));
        Assert.assertEquals("[1,2]", text);
    }
    
    public void test_double() throws Exception {
        JSONArrayAdd udf = new JSONArrayAdd();
        
        String text = udf.evaluate("[]", "$", 1D, 2D);
        Assert.assertEquals("[1,2]", text);
    }
}
