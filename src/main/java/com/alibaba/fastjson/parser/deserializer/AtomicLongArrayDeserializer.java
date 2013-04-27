package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicLongArray;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;

public class AtomicLongArrayDeserializer implements ObjectDeserializer {

    public final static AtomicLongArrayDeserializer instance = new AtomicLongArrayDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        if (parser.getLexer().token() == JSONToken.NULL) {
            parser.getLexer().nextToken(JSONToken.COMMA);
            return null;
        }

        JSONArray array = new JSONArray();
        parser.parseArray(array);

        AtomicLongArray atomicArray = new AtomicLongArray(array.size());
        for (int i = 0; i < array.size(); ++i) {
            atomicArray.set(i, array.getLong(i));
        }

        return (T) atomicArray;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }
}
