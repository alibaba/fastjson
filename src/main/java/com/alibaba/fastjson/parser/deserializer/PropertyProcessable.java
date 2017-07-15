package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

/**
 * Created by wenshao on 15/07/2017.
 */
public interface PropertyProcessable extends ParseProcess {
    Type getType(String name);
    void apply(String name, Object value);
}
