package com.alibaba.fastjson.serializer;

import java.io.IOException;

import javax.management.ObjectName;

public class ObjectNameSerializer implements ObjectSerializer {

    public final static ObjectNameSerializer instance = new ObjectNameSerializer();

    public void write(JSONSerializer serializer, Object object) throws IOException {
        serializer.write(((ObjectName) object).toString());
    }

}
