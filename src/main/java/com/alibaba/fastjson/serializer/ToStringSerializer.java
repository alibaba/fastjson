package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class ToStringSerializer implements ObjectSerializer {

    public static final ToStringSerializer instance = new ToStringSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }
        
        String strVal = null;
        if (object instanceof Long 
           && (serializer.out.isEnabled(SerializerFeature.WriteClassName) 
            || SerializerFeature.isEnabled(features, SerializerFeature.WriteClassName))
           ) {
            strVal = new StringBuffer().append(object).append("L").toString();
        } else {
            strVal =object.toString();
        }
        out.writeString(strVal);
    }

}
