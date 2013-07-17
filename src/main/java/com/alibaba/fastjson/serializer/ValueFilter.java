package com.alibaba.fastjson.serializer;

public interface ValueFilter extends SerializeFilter {

    Object process(Object object, String name, Object value);
}
