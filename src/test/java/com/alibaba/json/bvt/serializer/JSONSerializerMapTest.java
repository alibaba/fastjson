package com.alibaba.json.bvt.serializer;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.SerializeConfig;

import junit.framework.TestCase;

@SuppressWarnings("deprecation")
public class JSONSerializerMapTest extends TestCase {

    public void test_0() throws Exception {
        SerializeConfig map = new SerializeConfig();

        Assert.assertFalse(0 == map.size());
        Assert.assertEquals(true, map.get(Integer.class) == IntegerCodec.instance);

        Assert.assertEquals(true, map.put(Integer.class, IntegerCodec.instance));
        Assert.assertEquals(true, map.put(Integer.class, IntegerCodec.instance));
        Assert.assertEquals(true, map.put(Integer.class, IntegerCodec.instance));

        Assert.assertEquals(true, map.get(Integer.class) == IntegerCodec.instance);

        Assert.assertFalse(0 == map.size());
    }
}
