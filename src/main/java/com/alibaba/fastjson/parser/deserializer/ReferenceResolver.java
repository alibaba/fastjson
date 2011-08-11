package com.alibaba.fastjson.parser.deserializer;


public interface ReferenceResolver {
    boolean resolve(Object value, Object reference);
}
