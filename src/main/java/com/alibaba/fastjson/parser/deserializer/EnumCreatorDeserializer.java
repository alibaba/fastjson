package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class EnumCreatorDeserializer implements ObjectDeserializer {
    private final Method creator;
    private final Class paramType;

    public EnumCreatorDeserializer(Method creator) {
        this.creator = creator;
        paramType = creator.getParameterTypes()[0];
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Object arg = parser.parseObject(paramType);
        try {
            return (T) creator.invoke(null, arg);
        } catch (IllegalAccessException e) {
            throw new JSONException("parse enum error", e);
        } catch (InvocationTargetException e) {
            throw new JSONException("parse enum error", e);
        }
    }

    public int getFastMatchToken() {
        return 0;
    }
}
