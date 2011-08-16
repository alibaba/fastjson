package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.UUID;

import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;

public class UUIDDeserializer implements ObjectDeserializer {
    public final static UUIDDeserializer instance = new UUIDDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultExtJSONParser parser, Type clazz, Object fieldName) {
        
        String name = (String) parser.parse();
        
        if (name == null) {
            return null;
        }
        
        return (T) UUID.fromString(name);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
