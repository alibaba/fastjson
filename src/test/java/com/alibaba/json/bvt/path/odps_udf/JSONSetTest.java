package com.alibaba.json.bvt.path.odps_udf;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.support.odps.udf.JSONSet;


public class JSONSetTest extends TestCase {
    public void test_udf() throws Exception {
        JSONSet udf = new JSONSet();
        
        String text = udf.evaluate("[0,1,2]", "$[1]", new Long(123));
        Assert.assertEquals("[0,123,2]", text);
    }
    
    public void test_double() throws Exception {
        JSONSet udf = new JSONSet();
        
        String text = udf.evaluate("[1,2,3]", "[0]", 123.2D);
        Assert.assertEquals("[123.2,2,3]", text);
    }
}
