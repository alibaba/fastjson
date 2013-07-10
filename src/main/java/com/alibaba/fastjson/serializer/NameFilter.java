package com.alibaba.fastjson.serializer;

public interface NameFilter extends SerializeFilter {
    String process(Object source, String name, Object value);
}
