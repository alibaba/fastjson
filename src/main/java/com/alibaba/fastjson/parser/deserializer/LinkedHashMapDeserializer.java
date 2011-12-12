package com.alibaba.fastjson.parser.deserializer;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapDeserializer extends HashMapDeserializer implements ObjectDeserializer {

    public final static LinkedHashMapDeserializer instance = new LinkedHashMapDeserializer();

    protected Map<Object, Object> createMap() {
        Map<Object, Object> map = new LinkedHashMap<Object, Object>();
        return map;
    }

}
