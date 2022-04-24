package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class Issue4013 {

    @Test
    public void testNull(){
        Assert.assertNull(JSON.parseObject("null", true));
        Assert.assertNull(JSON.parseObject(" null ", true));
        Assert.assertNull(JSON.parseObject("Null", true));
        Assert.assertNull(JSON.parseObject("NULL", true));
        try {
            JSON.parseObject("null",false);
            fail();
        }
        catch (JSONException e){
            Assert.assertEquals(e.getMessage(),"value invalid: the input string is null");
        }
    }

    @Test
    public void testUndefined(){
        Assert.assertNull(JSON.parseObject("undefined", true,true));
        Assert.assertNull(JSON.parseObject(" undefined ", true,true));
        try {
            JSON.parseObject("undefined",true,false);
            fail();
        }
        catch (JSONException e){
            Assert.assertEquals(e.getMessage(),"value invalid: a JSON string or a JSON object cannot be undefined");
        }
    }
}
