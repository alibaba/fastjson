package com.alibaba.fastjson.serializer;

import java.lang.reflect.Type;

@Deprecated
public class JSONSerializerMap extends SerializeConfig {
    public boolean put(Type key, ObjectSerializer value) {
        return super.put(key, value);
    }
}
