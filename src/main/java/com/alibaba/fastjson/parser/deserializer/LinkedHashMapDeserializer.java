package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;

public class LinkedHashMapDeserializer implements ObjectDeserializer {
    public final static LinkedHashMapDeserializer instance = new LinkedHashMapDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        parser.parseObject(map);
        return (T) map;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
