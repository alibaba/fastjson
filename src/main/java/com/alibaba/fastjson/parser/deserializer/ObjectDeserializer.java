package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultJSONParser;

public interface ObjectDeserializer {
    <T> T deserialize(DefaultJSONParser parser, Type type, Object fieldName);
    
    int getFastMatchToken();
}
