package com.alibaba.fastjson.serializer;


public interface ContextValueFilter extends SerializeFilter {
    Object process(SerilaizeContext context, Object object, String name, Object value);
}
