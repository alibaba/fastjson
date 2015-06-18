package com.alibaba.json.bvt.bug;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_dubbo_long extends TestCase {

    public void test_0() throws Exception {
        Long val = 2345L;

        String text = JSON.toJSONString(val, SerializerFeature.WriteClassName);
        Assert.assertEquals(val, JSON.parseObject(text, long.class));
    }
}
