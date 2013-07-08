package com.alibaba.fastjson.parser.deserializer;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;

public class ReferenceDeserializer implements ObjectDeserializer {

    public final static ReferenceDeserializer instance = new ReferenceDeserializer();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        ParameterizedType paramType = (ParameterizedType) type;
        Type itemType = paramType.getActualTypeArguments()[0];

        Object itemObject = parser.parseObject(itemType);

        Type rawType = paramType.getRawType();
        if (rawType == AtomicReference.class) {
            return (T) new AtomicReference(itemObject);
        }

        if (rawType == WeakReference.class) {
            return (T) new WeakReference(itemObject);
        }

        if (rawType == SoftReference.class) {
            return (T) new SoftReference(itemObject);
        }

        throw new UnsupportedOperationException(rawType.toString());
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
