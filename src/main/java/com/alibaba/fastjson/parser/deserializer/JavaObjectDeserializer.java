package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;

public class JavaObjectDeserializer implements ObjectDeserializer {

    public final static JavaObjectDeserializer instance = new JavaObjectDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultExtJSONParser parser, Type clazz, Object fieldName) {
        return (T) parser.parse();
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
