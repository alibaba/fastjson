package com.alibaba.fastjson.serializer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.alibaba.fastjson.util.FieldInfo;

public class SerilaizeContext {
    private final FieldInfo fieldInfo;
    
    SerilaizeContext(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }
    
    public Method getMethod() {
        return fieldInfo.method;
    }
    
    public Field getField() {
        return fieldInfo.field;
    }
    
    public <T extends Annotation> T getAnnation(Class<T> annotationClass) {
        return fieldInfo.getAnnation(annotationClass);
    }
}
