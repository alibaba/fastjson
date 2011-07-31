package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.util.TypeUtils;

public class CharacterDeserializer implements ObjectDeserializer {
    public final static CharacterDeserializer instance = new CharacterDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultExtJSONParser parser, Type clazz) {
        Object value = parser.parse();

        if (value == null) {
            return null;
        }
        
        return (T) TypeUtils.castToChar(value);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
