package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.util.FieldInfo;

public abstract class FieldDeserializer {

    protected final FieldInfo fieldInfo;

    protected final Class<?>  clazz;

    public FieldDeserializer(Class<?> clazz, FieldInfo fieldInfo){
        this.clazz = clazz;
        this.fieldInfo = fieldInfo;
    }

    public Method getMethod() {
        return fieldInfo.getMethod();
    }

    public Class<?> getFieldClass() {
        return fieldInfo.getFieldClass();
    }

    public Type getFieldType() {
        return fieldInfo.getFieldType();
    }

    public abstract void parseField(DefaultExtJSONParser parser, Object object);

    public abstract int getFastMatchToken();

    public void setValue(Object object, boolean value) {
        setValue(object, Boolean.valueOf(value));
    }

    public void setValue(Object object, int value) {
        setValue(object, Integer.valueOf(value));
    }

    public void setValue(Object object, long value) {
        setValue(object, Long.valueOf(value));
    }

    public void setValue(Object object, String value) {
        setValue(object, (Object) value);
    }

    public void setValue(Object object, Object value) {
        try {
            fieldInfo.getMethod().invoke(object, value);
        } catch (Exception e) {
            throw new JSONException("set property error, " + fieldInfo.getMethod().toString(), e);
        }
    }
}
