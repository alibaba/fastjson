package com.alibaba.fastjson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;

public class TypeReference<T> {

    protected final Type type;

    protected TypeReference(){
        Type superClass = getClass().getGenericSuperclass();

        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }
    
    /**
     * @since 1.2.9
     * @param actualTypeArguments
     */
    protected TypeReference(Type... actualTypeArguments){
        Type superClass = getClass().getGenericSuperclass();

        ParameterizedType argType = (ParameterizedType) ((ParameterizedType) superClass).getActualTypeArguments()[0];
        Type rawType = argType.getRawType();
        Type[] argTypes = argType.getActualTypeArguments();
        
        int actualIndex = 0;
        for (int i = 0; i < argTypes.length; ++i) {
            if (argTypes[i] instanceof TypeVariable) {
                argTypes[i] = actualTypeArguments[actualIndex++];
                if (actualIndex >= actualTypeArguments.length) {
                    break;
                }
            }
        }
        type = new ParameterizedTypeImpl(argTypes, this.getClass(), rawType);
    }
    
    public Type getType() {
        return type;
    }
}
