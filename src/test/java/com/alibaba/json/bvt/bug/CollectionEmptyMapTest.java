package com.alibaba.json.bvt.bug;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class CollectionEmptyMapTest extends TestCase {
    public void test_0() throws Exception {
        Map<String, Object> map = Collections.emptyMap();
        
        String text = JSON.toJSONString(map, SerializerFeature.WriteClassName);
        
        Assert.assertEquals("{\"@type\":\"java.util.Collections$EmptyMap\"}", text);
        
        Assert.assertSame(map, JSON.parse(text));
    }
}
