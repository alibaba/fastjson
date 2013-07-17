package com.alibaba.fastjson.serializer;

public interface NameFilter extends SerializeFilter {
    String process(Object object, String name, Object value);
}
