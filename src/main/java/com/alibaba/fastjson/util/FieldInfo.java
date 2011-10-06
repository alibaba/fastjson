package com.alibaba.fastjson.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class FieldInfo implements Comparable<FieldInfo> {

    private final String   name;
    private final Method   method;
    private final Field    field;

    private final Class<?> fieldClass;
    private final Type     fieldType;
    private final Class<?> declaringClass;

    public FieldInfo(String name, Class<?> declaringClass, Class<?> fieldClass, Type fieldType, Method method,
                     Field field){
        this.name = name;
        this.declaringClass = declaringClass;
        this.fieldClass = fieldClass;
        this.fieldType = fieldType;
        this.method = method;
        this.field = field;

        if (method != null) {
            method.setAccessible(true);
        }

        if (field != null) {
            field.setAccessible(true);
        }
    }

    public FieldInfo(String name, Method method, Field field){
        this.name = name;
        this.method = method;
        this.field = field;

        if (method != null) {
            method.setAccessible(true);
        }

        if (field != null) {
            field.setAccessible(true);
        }

        if (method != null) {
            if (method.getParameterTypes().length == 1) {
                this.fieldClass = method.getParameterTypes()[0];
                this.fieldType = method.getGenericParameterTypes()[0];
            } else {
                this.fieldClass = method.getReturnType();
                this.fieldType = method.getGenericReturnType();
            }
            this.declaringClass = method.getDeclaringClass();
        } else {
            this.fieldClass = field.getType();
            this.fieldType = field.getGenericType();
            this.declaringClass = field.getDeclaringClass();
        }
    }

    public Class<?> getDeclaringClass() {
        return declaringClass;
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
        if (method != null) {
            annotation = method.getAnnotation(annotationClass);
        }

        if (annotation == null) {
            if (field != null) {
                annotation = field.getAnnotation(annotationClass);
            }
        }

        return annotation;
    }

    public Object get(Object javaObject) throws IllegalAccessException, InvocationTargetException {
        if (method != null) {
            Object value = method.invoke(javaObject, new Object[0]);
            return value;
        }

        return field.get(javaObject);
    }

    public void setAccessible(boolean flag) throws SecurityException {
        if (method != null) {
            method.setAccessible(flag);
            return;
        }

        field.setAccessible(flag);
    }
}
