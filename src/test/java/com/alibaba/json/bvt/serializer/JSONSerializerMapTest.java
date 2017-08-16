package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IdentityWeakHashMap;
import junit.framework.TestCase;
import org.junit.Assert;

import java.lang.reflect.Field;

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
    
    public static int size(SerializeConfig config) throws Exception {
        Field serializersField = SerializeConfig.class.getDeclaredField("serializers");
        serializersField.setAccessible(true);
        Object map = serializersField.get(config);
        
        Field bucketsField = IdentityWeakHashMap.class.getDeclaredField("buckets");
        bucketsField.setAccessible(true);
        Object[] buckets = (Object[]) bucketsField.get(map);
        
        Field nextField = Class.forName("com.alibaba.fastjson.util.IdentityWeakHashMap$Entry").getDeclaredField("next");
        
        int size = 0;
        for (int i = 0; i < buckets.length; ++i) {
            for (Object entry = buckets[i]; entry != null; entry = nextField.get(entry)) {
                size++;
            }
        }
        return size;
    }
}
