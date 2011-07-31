package com.alibaba.fastjson.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class FieldInfo implements Comparable<FieldInfo> {

    private final String   name;
    private final Method   method;
    private final Field    field;

    private final Class<?> fieldClass;
    private final Type     fieldType;

    public FieldInfo(String name, Method method, Field field){
        this.name = name;
        this.method = method;
        this.field = field;

        if (method.getParameterTypes().length == 1) {
            this.fieldClass = method.getParameterTypes()[0];
            this.fieldType = method.getGenericParameterTypes()[0];
        } else {
            this.fieldClass = method.getReturnType();
            this.fieldType = method.getGenericReturnType();
        }
    }

    public Class<?> getFieldClass() {
        return fieldClass;
    }

    public Type getFieldType() {
        return fieldType;
    }

    public String getName() {
        return name;
    }

    public Method getMethod() {
        return method;
    }

    public Field getField() {
        return field;
    }

    public int compareTo(FieldInfo o) {
        return this.name.compareTo(o.name);
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        T annotation = null;
        annotation = method.getAnnotation(annotationClass);

        if (annotation == null) {
            if (field != null) {
                annotation = field.getAnnotation(annotationClass);
            }
        }

        return annotation;
    }
}
