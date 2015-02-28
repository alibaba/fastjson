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
}
