package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.util.EnumMap;

/**
 * Created by wenshao on 2016/10/18.
 */
public class EnumMapTest extends TestCase {
    public void test_for_enum_map() throws Exception {
        EnumMap<Type, String> enumMap = new EnumMap<Type, String>(Type.class);
        enumMap.put(Type.Big, "BIG");

        String json = JSON.toJSONString(enumMap);
        System.out.println(json);
        EnumMap<Type, String> enumMap2 = JSON.parseObject(json, new TypeReference<EnumMap<Type, String>>(){});
        assertEquals(1, enumMap2.size());
        assertEquals(enumMap.get(Type.Big), enumMap2.get(Type.Big));
    }

    public static enum Type {
        Big, Small
    }
}
