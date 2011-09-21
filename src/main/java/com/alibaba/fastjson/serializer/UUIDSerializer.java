package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.UUID;

public class UUIDSerializer implements ObjectSerializer {

    public final static UUIDSerializer instance = new UUIDSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.writeNull();
            return;
        }

        UUID uid = (UUID) object;
        serializer.write(uid.toString());
    }

}
