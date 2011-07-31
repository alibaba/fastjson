package com.alibaba.fastjson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class TypeReference<T> {

    private final Type type;

    protected TypeReference(){
        Type superClass = getClass().getGenericSuperclass();

        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }
    
    public final static Type LIST_STRING = new TypeReference<List<String>>() {}.getType();
}
