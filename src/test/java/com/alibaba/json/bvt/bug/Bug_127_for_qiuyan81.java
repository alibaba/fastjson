package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class Bug_127_for_qiuyan81 extends TestCase {

    public void test_parserUndefined() {
        String jsonString = "{PayStatus:0,RunEmpId:undefined}";
        Object json = JSON.parse(jsonString);
        Assert.assertEquals("{\"PayStatus\":0}", json.toString());
    }
    
    public void test_parserUndefined_space() {
        String jsonString = "{PayStatus:0,RunEmpId:undefined }";
        Object json = JSON.parse(jsonString);
        Assert.assertEquals("{\"PayStatus\":0}", json.toString());
    }
    
    public void test_parserUndefined_comma() {
        String jsonString = "{PayStatus:0,RunEmpId:undefined,ext:1001}";
        JSONObject json = (JSONObject) JSON.parse(jsonString);
        Assert.assertEquals(1001, json.get("ext"));
        Assert.assertEquals(0, json.get("PayStatus"));
        Assert.assertEquals(3, json.size());
    }
    
    public void test_parserUndefined_array() {
        String jsonString = "[0,undefined]";
        Object json = JSON.parse(jsonString);
        Assert.assertEquals("[0,null]", json.toString());
    }
    
    public void test_parserUndefined_n() {
        String jsonString = "{PayStatus:0,RunEmpId:undefined\n}";
        Object json = JSON.parse(jsonString);
        Assert.assertEquals("{\"PayStatus\":0}", json.toString());
    }
    
    public void test_parserUndefined_r() {
        String jsonString = "{PayStatus:0,RunEmpId:undefined\r}";
        Object json = JSON.parse(jsonString);
        Assert.assertEquals("{\"PayStatus\":0}", json.toString());
    }
    
    public void test_parserUndefined_t() {
        String jsonString = "{PayStatus:0,RunEmpId:undefined\t}";
        Object json = JSON.parse(jsonString);
        Assert.assertEquals("{\"PayStatus\":0}", json.toString());
    }
    
    public void test_parserUndefined_f() {
        String jsonString = "{PayStatus:0,RunEmpId:undefined\f}";
        Object json = JSON.parse(jsonString);
        Assert.assertEquals("{\"PayStatus\":0}", json.toString());
    }
    
    public void test_parserUndefined_b() {
        String jsonString = "{PayStatus:0,RunEmpId:undefined\b}";
        Object json = JSON.parse(jsonString);
        Assert.assertEquals("{\"PayStatus\":0}", json.toString());
    }
    
    public void test_parserUndefined_single() {
        String jsonString = "undefined";
        Object json = JSON.parse(jsonString);
        Assert.assertNull(json);
    }
    
    public void test_parserUndefined_field() {
        String jsonString = "{undefined:1001}";
        Object json = JSON.parse(jsonString);
        Assert.assertEquals(1001, ((JSONObject)json).get("undefined"));
    }

    public void test_parserError() {
        Exception error = null;
        try {
            String jsonString = "{PayStatus:0,RunEmpId:undefinedaa}";
            JSON.parse(jsonString);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
