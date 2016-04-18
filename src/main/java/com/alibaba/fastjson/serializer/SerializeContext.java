package com.alibaba.fastjson.serializer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.alibaba.fastjson.util.FieldInfo;

/**
 * @since 1.2.9
 *
 */
public final class SerializeContext {

    private final Class<?>  beanClass;
    private final FieldInfo fieldInfo;

    SerializeContext(Class<?> beanClass, FieldInfo fieldInfo){
        this.beanClass = beanClass;
        this.fieldInfo = fieldInfo;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public Method getMethod() {
        return fieldInfo.method;
    }

    public Field getField() {
        return fieldInfo.field;
    }
    
    public String getName() {
        return fieldInfo.name;
    }
    
    public String getLabel() {
        return fieldInfo.label;
    }
    
    public int getFeatures() {
        return fieldInfo.serialzeFeatures;
    }

    public <T extends Annotation> T getAnnation(Class<T> annotationClass) {
        return fieldInfo.getAnnation(annotationClass);
    }
}
