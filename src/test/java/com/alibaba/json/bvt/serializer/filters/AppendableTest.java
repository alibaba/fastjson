package com.alibaba.json.bvt.serializer.filters;

import java.io.StringWriter;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;


public class AppendableTest extends TestCase {

    public void test_stringbufer() throws Exception {
        Appendable obj = new StringBuffer();
        obj.append("abc");
        
        String text = JSON.toJSONString(obj);
        
        Assert.assertEquals("\"abc\"", text);     
        

    }
    
    public void test_stringwriter() throws Exception {
        Appendable obj = new StringWriter();
        obj.append("abc");
        
        String text = JSON.toJSONString(obj);
        
        Assert.assertEquals("\"abc\"", text);     
    }
}
