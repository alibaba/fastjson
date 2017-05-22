package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class Bug_for_issue_423 extends TestCase {

    public void test_for_issue() throws Exception {
        String text = "[[],{\"value\":[]}]";
        Object root = JSON.parse(text, Feature.UseObjectArray);
        Assert.assertEquals(Object[].class, root.getClass());
        
        Object[] rootArray = (Object[]) root;
        Assert.assertEquals(Object[].class, rootArray[0].getClass());
        Assert.assertEquals(Object[].class, ((JSONObject)rootArray[1]).get("value").getClass());
    }

}
