package com.alibaba.fastjson.serializer;


public interface ContextValueFilter extends SerializeFilter {
    Object process(FieldContext context, Object object, String name, Object value);
}
