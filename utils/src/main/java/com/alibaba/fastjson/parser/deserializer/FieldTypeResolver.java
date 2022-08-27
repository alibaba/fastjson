package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

public interface FieldTypeResolver extends ParseProcess {
    Type resolve(Object object, String fieldName);
}
