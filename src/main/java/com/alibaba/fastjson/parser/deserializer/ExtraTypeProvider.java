package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;


public interface ExtraTypeProvider extends ParseProcess {
    Type getExtraType(Object object, String key);
}
