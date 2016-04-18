package com.alibaba.fastjson.serializer;

/**
 * @since 1.2.9
 *
 */
public interface ContextValueFilter extends SerializeFilter {
    Object process(SerializeContext context, Object object, String name, Object value);
}
