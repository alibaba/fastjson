package com.alibaba.fastjson.serializer;

import java.lang.annotation.Annotation;

import com.alibaba.fastjson.util.FieldInfo;

public class FieldContext {
    private final FieldInfo fieldInfo;
    
    protected FieldContext(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }
    
    public <T extends Annotation> T getAnnation(Class<T> annotationClass) {
        return fieldInfo.getAnnation(annotationClass);
    }
}
