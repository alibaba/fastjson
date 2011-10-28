package com.alibaba.fastjson.serializer;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Type;


public class ColorSerializer implements ObjectSerializer {
    public final static ColorSerializer instance = new ColorSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Color color = (Color) object;
        if (color == null) {
            out.writeNull();
            return;
        }
        
        char sep = '{';
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            out.write('{');
            out.writeFieldName("@type");
            out.writeString(Color.class.getName());
            sep = ',';
        }
        
        out.writeFieldValue(sep, "r", color.getRed());
        out.writeFieldValue(',', "g", color.getGreen());
        out.writeFieldValue(',', "b", color.getBlue());
        out.write('}');
    }

}
