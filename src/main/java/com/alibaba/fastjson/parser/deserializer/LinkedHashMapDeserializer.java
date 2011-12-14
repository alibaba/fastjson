package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapDeserializer extends MapDeserializer implements ObjectDeserializer {

    public final static LinkedHashMapDeserializer instance = new LinkedHashMapDeserializer();

    protected Map<Object, Object> createMap(Type type) {
        Map<Object, Object> map = new LinkedHashMap<Object, Object>();
        return map;
    }

}
