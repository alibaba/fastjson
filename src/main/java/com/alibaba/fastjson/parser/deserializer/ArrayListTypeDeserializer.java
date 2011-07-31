package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;

public class ArrayListTypeDeserializer implements ObjectDeserializer {

    private Type itemType;

    public ArrayListTypeDeserializer(Type type){
        this.itemType = type;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T deserialze(DefaultExtJSONParser parser, Type type) {
        ArrayList list = new ArrayList();

        parser.parseArray(itemType, list);

        return (T) list;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }

}
