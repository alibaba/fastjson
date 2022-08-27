package com.alibaba.fastjson.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import com.alibaba.fastjson.parser.ParserConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by jiangyu on 2017-03-03 11:33.
 */
public class IgnoreTypeDeserializer {
    
    @Before
    public void before() {
        ParserConfig.global.setAutoTypeSupport(true);
    }

    @After
    public void after() {
        ParserConfig.global.setAutoTypeSupport(false);
    }

    @Test(expected = JSONException.class)
    public void parseObjectWithNotExistTypeThrowException() {
        String s = "{\"@type\":\"com.alibaba.fastjson.ValueBean\",\"val\":1}";
        JSONObject.parseObject(s, ValueBean.class);
    }

    @Test
    public void parseObjectWithNotExistType() {
        String s = "{\"@type\":\"com.alibaba.fastjson.ValueBean\",\"val\":1}";
        ValueBean v = JSONObject.parseObject(s, ValueBean.class, Feature.IgnoreAutoType);
        Assert.assertNotNull(v);
        Assert.assertEquals(new Integer(1), v.getVal());
    }

    @Test
    public void parseWithNotExistType() {
        String s = "{\"@type\":\"com.alibaba.fastjson.ValueBean\",\"val\":1}";
        Object object = JSONObject.parse(s);
        Assert.assertNotNull(object);
        Assert.assertTrue(object instanceof JSONObject);
        Assert.assertEquals(new Integer(1), JSONObject.class.cast(object).getInteger("val"));
    }

    @Test
    public void parseWithExistType() {
        String s = "{\"@type\":\"com.alibaba.fastjson.deserializer.ValueBean\",\"val\":1}";
        Object object = JSONObject.parse(s);
        Assert.assertNotNull(object);
        Assert.assertTrue(object instanceof ValueBean);
        Assert.assertEquals(new Integer(1), ValueBean.class.cast(object).getVal());
    }

    @Test
    public void parseObjectWithExistType() {
        String s = "{\"@type\":\"com.alibaba.fastjson.deserializer.ValueBean\",\"val\":1}";
        ValueBean object = JSONObject.parseObject(s, ValueBean.class);
        Assert.assertNotNull(object);
        Assert.assertEquals(new Integer(1), object.getVal());
    }

}
