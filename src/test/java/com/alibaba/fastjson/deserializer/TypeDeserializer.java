package com.alibaba.fastjson.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jiangyu on 2017-03-03 11:33.
 */
public class TypeDeserializer {
    @Test(expected = JSONException.class)
    public void deserialize() {
        String s = "{\"@type\":\"com.alibaba.fastjson.ValueBean\",\"val\":1}";
        ValueBean v = JSONObject.parseObject(s, ValueBean.class);
    }

    @Test
    public void deserializeWithIgnoreType() {
        String s = "{\"@type\":\"com.alibaba.fastjson.ValueBean\",\"val\":1}";
        ValueBean v = JSONObject.parseObject(s, ValueBean.class,Feature.IgnoreType);
        Assert.assertNotNull(v);
        Assert.assertEquals(new Integer(1),v.getVal());
    }
}
