package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class LocaleCodec implements ObjectSerializer, ObjectDeserializer {

    public final static LocaleCodec instance = new LocaleCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.writeNull();
            return;
        }

        Locale locale = (Locale) object;
        serializer.write(locale.toString());
    }
    
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        String text = (String) parser.parse();
        
        if (text == null) {
            return null;
        }
        
        String[] items = text.split("_");
        
        if (items.length == 1) {
            return (T) new Locale(items[0]);
        }
        
        if (items.length == 2) {
            return (T) new Locale(items[0], items[1]);
        }
        
        return (T) new Locale(items[0], items[1], items[2]);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }

}
