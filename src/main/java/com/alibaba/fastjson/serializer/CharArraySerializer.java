package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;


public class CharArraySerializer implements ObjectSerializer {

    public static CharArraySerializer instance = new CharArraySerializer();

    public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        
        if (object == null) {
            if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
                out.write("[]");
            } else {
                out.writeNull();
            }
            return;
        }

        char[] chars = (char[]) object;
        out.writeString(new String(chars));
    }

}
