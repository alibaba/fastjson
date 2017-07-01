package com.alibaba.fastjson;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Represents a generic type {@code T}. Java doesn't yet provide a way to
 * represent generic types, so this class does. Forces clients to create a
 * subclass of this class which enables retrieval the type information even at
 * runtime.
 *
 * <p>For example, to create a type literal for List&lt;String&gt;, you can
 * create an empty anonymous inner class:
 *
 * <p>
 * List&lt;String&gt; list = JSON.parseObject("...", new TypeReference&lt;List&lt;String&gt;&gt;() {});
 *
 *
 * @author wenshao[szujobs@hotmail.com]
 */
public class TypeReference<T> {
    static ConcurrentMap<Type, Type> classTypeCache
            = new ConcurrentHashMap<Type, Type>(16, 0.75f, 1);

    protected final Type type;

    protected TypeReference(){
        Type superClass = getClass().getGenericSuperclass();

        Type oriType = ((ParameterizedType) superClass).getActualTypeArguments()[0];

        if (oriType instanceof Class) {
            type = oriType;
        } else {
            //修复在安卓环境中问题
            Type cachedType = classTypeCache.get(oriType);
            if (cachedType == null) {
                classTypeCache.putIfAbsent(oriType, oriType);
                cachedType = classTypeCache.get(oriType);
            }

            type = cachedType;
        }
    }

    /**
     * @since 1.2.9 & 1.1.58.android
     * @param actualTypeArguments
     */
    protected TypeReference(Type... actualTypeArguments){
        Class<?> thisClass = this.getClass();
        Type superClass = thisClass.getGenericSuperclass();

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

        Type key = new ParameterizedTypeImpl(argTypes, thisClass, rawType);
        Type cachedType = classTypeCache.get(key);
        if (cachedType == null) {
            classTypeCache.putIfAbsent(key, key);
            cachedType = classTypeCache.get(key);
        }

        type = cachedType;

    }
    
    public Type getType() {
        return type;
    }
}
