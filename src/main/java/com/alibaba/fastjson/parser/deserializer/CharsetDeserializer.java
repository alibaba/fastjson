package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;

public class CharsetDeserializer implements ObjectDeserializer {
    public final static CharsetDeserializer instance = new CharsetDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultExtJSONParser parser, Type clazz) {
        Object value = parser.parse();

        if (value == null) {
            return null;
        }
        
        String charset = (String) value;
        
        return (T) Charset.forName(charset);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
