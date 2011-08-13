package com.alibaba.fastjson.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DeserializeBeanInfo {

    private final Class<?>        clazz;
    private final Type            type;
    private Constructor<?>        defaultConstructor;
    private Constructor<?>        creatorConstructor;
    private Method                factoryMethod;

    private final List<FieldInfo> fieldList = new ArrayList<FieldInfo>();

    public DeserializeBeanInfo(Class<?> clazz){
        super();
        this.clazz = clazz;
        this.type = clazz;
    }

    public Constructor<?> getDefaultConstructor() {
        return defaultConstructor;
    }

    public void setDefaultConstructor(Constructor<?> defaultConstructor) {
        this.defaultConstructor = defaultConstructor;
    }

    public Constructor<?> getCreatorConstructor() {
        return creatorConstructor;
    }

    public void setCreatorConstructor(Constructor<?> createConstructor) {
        this.creatorConstructor = createConstructor;
    }

    public Method getFactoryMethod() {
        return factoryMethod;
    }

    public void setFactoryMethod(Method factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Type getType() {
        return type;
    }

    public List<FieldInfo> getFieldList() {
        return fieldList;
    }

}
