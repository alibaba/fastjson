package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.parser.JSONToken;

public class ConcurrentHashMapDeserializer extends MapDeserializer implements ObjectDeserializer {
    public final static ConcurrentHashMapDeserializer instance = new ConcurrentHashMapDeserializer();

	protected Map<Object, Object> createMap(Type type) {
		Map<Object, Object> map = new ConcurrentHashMap<Object, Object>();
		return map;
	}
	
    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
