package com.alibaba.fastjson.serializer;


@Deprecated
public class JSONSerializerMap extends SerializeConfig {
    public final boolean put(Class<?> clazz, ObjectSerializer serializer) {
        return super.put(clazz, serializer);
    }
}
