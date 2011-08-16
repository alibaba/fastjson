package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultExtJSONParser;

public interface ObjectDeserializer {
    <T> T deserialze(DefaultExtJSONParser parser, Type type, Object fieldName);
    
    int getFastMatchToken();
}
