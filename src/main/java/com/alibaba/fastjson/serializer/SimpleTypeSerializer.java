package com.alibaba.fastjson.serializer;

import java.io.IOException;

import javax.management.openmbean.SimpleType;

public class SimpleTypeSerializer implements ObjectSerializer {

    public final static SimpleTypeSerializer instance = new SimpleTypeSerializer();

    @SuppressWarnings("rawtypes")
    public void write(JSONSerializer serializer, Object object) throws IOException {
        SimpleType type = (SimpleType) object;
        serializer.write(type.getTypeName());
    }

}
