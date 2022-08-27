package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public interface PropertyProcessable extends ParseProcess {
    /**
     * provide property's type, {@code java.lang.Class} or {@code Type}
     * @param name property name
     * @return property's type
     */
    Type getType(String name);

    /**
     * apply property name and value
     * @param name property name
     * @param value property name
     */
    void apply(String name, Object value);
}
