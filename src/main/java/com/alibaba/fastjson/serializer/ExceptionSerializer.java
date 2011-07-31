package com.alibaba.fastjson.serializer;

public class ExceptionSerializer extends JavaBeanSerializer {

    public ExceptionSerializer(Class<?> clazz){
        super(clazz);
    }

    protected boolean isWriteClassName(JSONSerializer serializer) {
        return true;
    }
}
