package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.util.FieldInfo;

public abstract class FieldDeserializer {

    public final FieldInfo fieldInfo;

    protected final Class<?>  clazz;

    public FieldDeserializer(Class<?> clazz, FieldInfo fieldInfo){
        this.clazz = clazz;
        this.fieldInfo = fieldInfo;
    }
    
    public abstract void parseField(DefaultJSONParser parser, Object object, Type objectType,
                                    Map<String, Object> fieldValues);

    public int getFastMatchToken() {
        return 0;
    }

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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setValue(Object object, Object value) {
        Method method = fieldInfo.method;
        if (method != null) {
            try {
                if (fieldInfo.getOnly) {
                    if (fieldInfo.fieldClass == AtomicInteger.class) {
                        AtomicInteger atomic = (AtomicInteger) method.invoke(object);
                        if (atomic != null) {
                            atomic.set(((AtomicInteger) value).get());
                        }
                    } else if (fieldInfo.fieldClass == AtomicLong.class) {
                        AtomicLong atomic = (AtomicLong) method.invoke(object);
                        if (atomic != null) {
                            atomic.set(((AtomicLong) value).get());
                        }
                    } else if (fieldInfo.fieldClass == AtomicBoolean.class) {
                        AtomicBoolean atomic = (AtomicBoolean) method.invoke(object);
                        if (atomic != null) {
                            atomic.set(((AtomicBoolean) value).get());
                        }
                    } else if (Map.class.isAssignableFrom(method.getReturnType())) {
                        Map map = (Map) method.invoke(object);
                        if (map != null) {
                            map.putAll((Map) value);
                        }
                    } else {
                        Collection collection = (Collection) method.invoke(object);
                        if (collection != null) {
                            collection.addAll((Collection) value);
                        }
                    }
                } else {
                    if (value == null && fieldInfo.fieldClass.isPrimitive()) {
                        return;
                    }
                    method.invoke(object, value);
                }
            } catch (Exception e) {
                throw new JSONException("set property error, " + fieldInfo.name, e);
            }
            return;
        }

        final Field field = fieldInfo.field;
        if (field != null) {
            try {
                field.set(object, value);
            } catch (Exception e) {
                throw new JSONException("set property error, " + fieldInfo.name, e);
            }
        }
    }
}
