package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;

public class TreeMapDeserializer implements ObjectDeserializer {
    public final static TreeMapDeserializer instance = new TreeMapDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultExtJSONParser parser, Type clazz, Object fieldName) {
        Map<String, Object> map = new TreeMap<String, Object>();
        parser.parseObject(map);
        return (T) map;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
