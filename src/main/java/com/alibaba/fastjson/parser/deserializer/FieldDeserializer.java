package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.util.FieldInfo;

public abstract class FieldDeserializer {

    public final FieldInfo fieldInfo;

    protected final Class<?> clazz;

    protected BeanContext beanContext;

    public FieldDeserializer(Class<?> clazz, FieldInfo fieldInfo) {
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setValue(Object object, Object value) {
        if (value == null //
                && fieldInfo.fieldClass.isPrimitive()) {
            return;
        } else if (fieldInfo.fieldClass == String.class
                && fieldInfo.format != null
                && fieldInfo.format.equals("trim")) {
            value = ((String) value).trim();
        }

        try {
            Method method = fieldInfo.method;
            if (method != null) {
                if (fieldInfo.getOnly) {
                    if (fieldInfo.fieldClass == AtomicInteger.class) {
                        AtomicInteger atomic = (AtomicInteger) method.invoke(object);
                        if (atomic != null) {
                            atomic.set(((AtomicInteger) value).get());
                        } else {
                            degradeValueAssignment(fieldInfo.field, method, object, value);
                        }
                    } else if (fieldInfo.fieldClass == AtomicLong.class) {
                        AtomicLong atomic = (AtomicLong) method.invoke(object);
                        if (atomic != null) {
                            atomic.set(((AtomicLong) value).get());
                        } else {
                            degradeValueAssignment(fieldInfo.field, method, object, value);
                        }
                    } else if (fieldInfo.fieldClass == AtomicBoolean.class) {
                        AtomicBoolean atomic = (AtomicBoolean) method.invoke(object);
                        if (atomic != null) {
                            atomic.set(((AtomicBoolean) value).get());
                        } else {
                            degradeValueAssignment(fieldInfo.field, method, object, value);
                        }
                    } else if (Map.class.isAssignableFrom(method.getReturnType())) {
                        Map map = null;
                        try {
                            map = (Map) method.invoke(object);
                        } catch (InvocationTargetException e) {
                            degradeValueAssignment(fieldInfo.field, method, object, value);
                            return;
                        }
                        if (map != null) {
                            if (map == Collections.emptyMap()
                                    || map.getClass().getName().startsWith("java.util.Collections$Unmodifiable")) {
                                // skip
                                return;
                            }

                            if (map.getClass().getName().equals("kotlin.collections.EmptyMap")) {
                                degradeValueAssignment(fieldInfo.field, method, object, value);
                                return;
                            }

                            map.putAll((Map) value);
                        } else if (value != null) {
                            degradeValueAssignment(fieldInfo.field, method, object, value);
                        }
                    } else {
                        Collection collection = null;
                        try {
                            collection = (Collection) method.invoke(object);
                        } catch (InvocationTargetException e) {
                            degradeValueAssignment(fieldInfo.field, method, object, value);
                            return;
                        }
                        if (collection != null && value != null) {
                            String collectionClassName = collection.getClass().getName();
                            if (collection == Collections.emptySet()
                                    || collection == Collections.emptyList()
                                    || collectionClassName.startsWith("java.util.Collections$Unmodifiable")) {
                                // skip
                                return;
                            }

                            if (!collection.isEmpty()) {
                                collection.clear();
                            }

                            if (collectionClassName.equals("kotlin.collections.EmptyList")
                                    || collectionClassName.equals("kotlin.collections.EmptySet")) {
                                degradeValueAssignment(fieldInfo.field, method, object, value);
                                return;
                            }
                            collection.addAll((Collection) value);
                        } else if (collection == null && value != null) {
                            degradeValueAssignment(fieldInfo.field, method, object, value);
                        }
                    }
                } else {
                    method.invoke(object, value);
                }
            } else {
                final Field field = fieldInfo.field;
                
                if (fieldInfo.getOnly) {
                    if (fieldInfo.fieldClass == AtomicInteger.class) {
                        AtomicInteger atomic = (AtomicInteger) field.get(object);
                        if (atomic != null) {
                            atomic.set(((AtomicInteger) value).get());
                        }
                    } else if (fieldInfo.fieldClass == AtomicLong.class) {
                        AtomicLong atomic = (AtomicLong) field.get(object);
                        if (atomic != null) {
                            atomic.set(((AtomicLong) value).get());
                        }
                    } else if (fieldInfo.fieldClass == AtomicBoolean.class) {
                        AtomicBoolean atomic = (AtomicBoolean) field.get(object);
                        if (atomic != null) {
                            atomic.set(((AtomicBoolean) value).get());
                        }
                    } else if (Map.class.isAssignableFrom(fieldInfo.fieldClass)) {
                        Map map = (Map) field.get(object);
                        if (map != null) {
                            if (map == Collections.emptyMap()
                                    || map.getClass().getName().startsWith("java.util.Collections$Unmodifiable")) {
                                // skip
                                return;
                            }
                            map.putAll((Map) value);
                        }
                    } else {
                        Collection collection = (Collection) field.get(object);
                        if (collection != null && value != null) {
                            if (collection == Collections.emptySet()
                                    || collection == Collections.emptyList()
                                    || collection.getClass().getName().startsWith("java.util.Collections$Unmodifiable")) {
                                // skip
                                return;
                            }

                            collection.clear();
                            collection.addAll((Collection) value);
                        }
                    }
                } else {
                    if (field != null) {
                        field.set(object, value);
                    }
                }
            }
        } catch (Exception e) {
            throw new JSONException("set property error, " + clazz.getName() + "#" + fieldInfo.name, e);
        }
    }

    /**
     * kotlin代理类property的get方法会抛未初始化异常，用set方法直接赋值
     */
    private static void degradeValueAssignment(Field field,Method getMethod, Object object, Object value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (setFieldValue(field, object, value)) {
            return;
        }
        Method setMethod = object.getClass().getDeclaredMethod("set" + getMethod.getName().substring(3), getMethod.getReturnType());
        setMethod.invoke(object, value);
    }

    private static boolean setFieldValue(Field field, Object object, Object value) throws IllegalAccessException {
        if (field != null
                && !Modifier.isFinal(field.getModifiers())) {
            field.set(object, value);
            return true;
        }
        return false;
    }

    public void setWrappedValue(String key, Object value) {
        throw new JSONException("TODO");
    }
}
