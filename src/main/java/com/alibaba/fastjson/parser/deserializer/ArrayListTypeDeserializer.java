package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;

public class ArrayListTypeDeserializer implements ObjectDeserializer {

    private Type     itemType;
    private Class<?> rawClass;

    public ArrayListTypeDeserializer(Class<?> rawClass, Type itemType){
        this.rawClass = rawClass;
        this.itemType = itemType;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Collection list = null;

        if (parser.getLexer().token() == JSONToken.NULL) {
            parser.getLexer().nextToken();
        } else {
            if (rawClass.isAssignableFrom(LinkedHashSet.class)) {
                list = new LinkedHashSet();
            } else if (rawClass.isAssignableFrom(HashSet.class)) {
                list = new HashSet();
            } else {
                list = new ArrayList();
            }
            
            parser.parseArray(itemType, list, fieldName);
        }

        return (T) list;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }

}
