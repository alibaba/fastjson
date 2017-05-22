package com.alibaba.json.bvt.serializer.jsonp;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPObject;


public class JSONPObjectTest extends TestCase {
    public void test_jsonp() throws Exception {
        JSONPObject jsonp = new JSONPObject("checkValid");
        jsonp.addParameter(new HashMap<String, Object>());
        jsonp.addParameter(new ArrayList<Object>());
        
        String text = jsonp.toString();
        
        Assert.assertEquals("checkValid({},[])", text);
    }
    
    public void test_jsonp_1() throws Exception {
        JSONPObject jsonp = new JSONPObject();
        jsonp.setFunction("checkValid");
        jsonp.addParameter(new HashMap<String, Object>());
        jsonp.addParameter(new ArrayList<Object>());
        
        Assert.assertEquals("checkValid", jsonp.getFunction());
        Assert.assertEquals(2, jsonp.getParameters().size());
        
        String text = jsonp.toString();
        
        Assert.assertEquals("checkValid({},[])", text);
        Assert.assertEquals("checkValid({},[])", jsonp.toJSONString());
    }
}
