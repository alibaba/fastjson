package com.alibaba.json.bvt.parser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class CommentTest extends TestCase {

    public void test_0() throws Exception {
        String text = "{ // aa" //
                      + "\n}";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertEquals(0, obj.size());
    }

    public void test_1() throws Exception {
        String text = "{ // aa" //
                      + "\n\"value\":1001}";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertEquals(1, obj.size());
        Assert.assertEquals(1001, obj.get("value"));
    }

    public void test_2() throws Exception {
        String text = "{ /* aa */ \"value\":1001}";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertEquals(1, obj.size());
        Assert.assertEquals(1001, obj.get("value"));
    }

    public void test_3() throws Exception {
        String text = "{ \"value\":/* aa */1001}";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertEquals(1, obj.size());
        Assert.assertEquals(1001, obj.get("value"));
    }

    public void test_4() throws Exception {
        String text = "{ \"value\":1001/* aa */}";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertEquals(1, obj.size());
        Assert.assertEquals(1001, obj.get("value"));
    }

    public void test_5() throws Exception {
        Exception error = null;
        try {
            String text = "{ \"value\":1001/ * aa */}";
            JSON.parse(text);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_6() throws Exception {
        String text = "{'a':1, 'b':2 /***/ }";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertEquals(2, obj.size());
        Assert.assertEquals(1, obj.get("a"));
        Assert.assertEquals(2, obj.get("b"));
    }
    
    public void test_7() throws Exception {
        String text = "{'a':1, 'b':2 /**/ }";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertEquals(2, obj.size());
        Assert.assertEquals(1, obj.get("a"));
        Assert.assertEquals(2, obj.get("b"));
    }
}
