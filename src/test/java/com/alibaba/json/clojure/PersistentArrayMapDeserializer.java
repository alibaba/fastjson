package com.alibaba.json.clojure;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import clojure.lang.PersistentArrayMap;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.AutowiredObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.ConcurrentHashMapDeserializer;
import com.alibaba.fastjson.parser.deserializer.DefaultObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.HashMapDeserializer;

public class PersistentArrayMapDeserializer extends HashMapDeserializer implements AutowiredObjectDeserializer {

    public final static ConcurrentHashMapDeserializer instance = new ConcurrentHashMapDeserializer();

    protected Map<Object, Object> createMap() {
        Map<Object, Object> map = new ConcurrentHashMap<Object, Object>();
        return (Map<Object, Object>) PersistentArrayMap.create(map);
    }

    protected void deserialze(DefaultJSONParser parser, Type type, Object fieldName, Map<Object, Object> map) {
        DefaultObjectDeserializer.instance.parseMap(parser, map, Object.class, Object.class, fieldName);
    }

    public Set<Type> getAutowiredFor() {
        return Collections.<Type> singleton(PersistentArrayMap.class);
    }

}
