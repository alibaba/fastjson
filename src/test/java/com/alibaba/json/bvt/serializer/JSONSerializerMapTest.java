package com.alibaba.json.bvt.serializer;

import java.lang.reflect.Field;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IdentityHashMap;

import junit.framework.TestCase;

@SuppressWarnings("deprecation")
public class JSONSerializerMapTest extends TestCase {

    public void test_0() throws Exception {
        SerializeConfig map = new SerializeConfig();

        Assert.assertFalse(0 == size(map));
        Assert.assertEquals(true, map.get(Integer.class) == IntegerCodec.instance);

        Assert.assertEquals(true, map.put(Integer.class, IntegerCodec.instance));
        Assert.assertEquals(true, map.put(Integer.class, IntegerCodec.instance));
        Assert.assertEquals(true, map.put(Integer.class, IntegerCodec.instance));

        Assert.assertEquals(true, map.get(Integer.class) == IntegerCodec.instance);

        Assert.assertFalse(0 == size(map));
    }
    
    public static int size(IdentityHashMap map) throws Exception {
        Field bucketsField = IdentityHashMap.class.getDeclaredField("buckets");
        bucketsField.setAccessible(true);
        Object[] buckets = (Object[]) bucketsField.get(map);
        
        Field nextField = Class.forName("com.alibaba.fastjson.util.IdentityHashMap$Entry").getDeclaredField("next");
        
        int size = 0;
        for (int i = 0; i < buckets.length; ++i) {
            for (Object entry = buckets[i]; entry != null; entry = nextField.get(entry)) {
                size++;
            }
        }
        return size;
    }
}
