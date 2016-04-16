package com.alibaba.fastjson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

    protected final Type type;

    protected TypeReference(){
        Type superClass = getClass().getGenericSuperclass();

        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }
    
    public Type getType() {
        return type;
    }
}
