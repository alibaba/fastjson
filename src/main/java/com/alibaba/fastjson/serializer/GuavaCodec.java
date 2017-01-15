package com.alibaba.fastjson.serializer;

import com.google.common.collect.Multimap;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * Created by wenshao on 15/01/2017.
 */
public class GuavaCodec implements ObjectSerializer {
    public static GuavaCodec instance = new GuavaCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object instanceof Multimap) {
            Multimap multimap = (Multimap) object;
            serializer.write(multimap.asMap());
        }
    }
}
