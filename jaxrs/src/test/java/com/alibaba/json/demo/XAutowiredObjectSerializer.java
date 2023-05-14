package com.alibaba.json.demo;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

import com.alibaba.fastjson.serializer.AutowiredObjectSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;


public class XAutowiredObjectSerializer implements AutowiredObjectSerializer {

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        
    }

    public Set<Type> getAutowiredFor() {
        return Collections.<Type>singleton(X.class);
    }

}
