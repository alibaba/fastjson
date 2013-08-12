package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.JSONSerializerMap;

@SuppressWarnings("deprecation")
public class JSONSerializerMapTest extends TestCase {

    public void test_0() throws Exception {
        JSONSerializerMap map = new JSONSerializerMap();

        Assert.assertFalse(0 == map.size());
        Assert.assertEquals(true, map.get(Integer.class) == IntegerCodec.instance);

        Assert.assertEquals(true, map.put(Integer.class, IntegerCodec.instance));
        Assert.assertEquals(true, map.put(Integer.class, IntegerCodec.instance));
        Assert.assertEquals(true, map.put(Integer.class, IntegerCodec.instance));

        Assert.assertEquals(true, map.get(Integer.class) == IntegerCodec.instance);

        Assert.assertFalse(0 == map.size());
    }
}
