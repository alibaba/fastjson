package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Enumeration;


public class EnumerationSeriliazer implements ObjectSerializer {
    public static EnumerationSeriliazer instance = new EnumerationSeriliazer();
    
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();

        if (object == null) {
            if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
                out.write("[]");
            } else {
                out.writeNull();
            }
            return;
        }
        
        Type elementType = null;
        if (serializer.isEnabled(SerializerFeature.WriteClassName)) {
            if (fieldType instanceof ParameterizedType) {
                ParameterizedType param = (ParameterizedType) fieldType;
                elementType = param.getActualTypeArguments()[0];
            }
        }
        
        Enumeration<?> e = (Enumeration<?>) object;
        
        SerialContext context = serializer.getContext();
        serializer.setContext(context, object, fieldName, 0);

        try {
            int i = 0;
            out.append('[');
            while (e.hasMoreElements()) {
                Object item = e.nextElement();
                if (i++ != 0) {
                    out.append(',');
                }

                if (item == null) {
                    out.writeNull();
                    continue;
                }

                Class<?> clazz = item.getClass();

                ObjectSerializer itemSerializer = serializer.getObjectWriter(clazz);
                itemSerializer.write(serializer, item, i - 1, elementType);
            }
            out.append(']');
        } finally {
            serializer.setContext(context);
        }
    }
}
