package com.alibaba.fastjson;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

//CS304 Issue link: https://github.com/alibaba/fastjson/issues/4013
/**
 * A Test on Issue4013
 **/
public class Issue4013 {

    /**
     * test if the string --null can be generated into a JSON object correctly
     **/
    @Test
    public void testNull() {
        final String message = "test generate string --null into null JSON object";
        Assert.assertNull(message, JSON.parseObject("null", true));
        Assert.assertNull(message, JSON.parseObject(" null ", true));
        Assert.assertNull(message, JSON.parseObject("Null", true));
        Assert.assertNull(message, JSON.parseObject("NULL", true));
        try {
            JSON.parseObject("null", false);
            fail("only string --null is not allowed");
        } catch (JSONException e) {
            Assert.assertEquals("check error message", e.getMessage(), "value invalid: the input string is null");
        }
    }

    /**
     * test if the string --undefined can be generated into a JSON object correctly
     **/
    @Test
    public void testUndefined() {
        final String message = "test generate string --undefined into null JSON object";
        Assert.assertNull(message, JSON.parseObject("undefined", true, true));
        Assert.assertNull(message, JSON.parseObject(" undefined ", true, true));
        try {
            JSON.parseObject("undefined", true, false);
            fail("undefined is not allowed");
        } catch (JSONException e) {
            Assert.assertEquals("check error message", e.getMessage(), "value invalid: a JSON string or a JSON object cannot be undefined");
        }
    }
}
