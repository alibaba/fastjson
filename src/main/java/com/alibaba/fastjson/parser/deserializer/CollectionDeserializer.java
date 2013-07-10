package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;

public class CollectionDeserializer implements ObjectDeserializer {

    public final static CollectionDeserializer instance = new CollectionDeserializer();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (parser.getLexer().token() == JSONToken.NULL) {
            parser.getLexer().nextToken(JSONToken.COMMA);
            return null;
        }

        Class<?> rawClass = getRawClass(type);

        Collection list;
        if (rawClass == AbstractCollection.class) {
            list = new ArrayList();
        } else if (rawClass.isAssignableFrom(HashSet.class)) {
            list = new HashSet();
        } else if (rawClass.isAssignableFrom(LinkedHashSet.class)) {
            list = new LinkedHashSet();
        } else if (rawClass.isAssignableFrom(TreeSet.class)) {
            list = new TreeSet();
        } else if (rawClass.isAssignableFrom(ArrayList.class)) {
            list = new ArrayList();
        } else {
            try {
                list = (Collection) rawClass.newInstance();
            } catch (Exception e) {
                throw new JSONException("create instane error, class " + rawClass.getName());
            }
        }

        Type itemType;
        if (type instanceof ParameterizedType) {
            itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            itemType = Object.class;
        }
        parser.parseArray(itemType, list, fieldName);

        return (T) list;
    }

    public Class<?> getRawClass(Type type) {

        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType) type).getRawType());
        } else {
            throw new JSONException("TODO");
        }
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }
}
