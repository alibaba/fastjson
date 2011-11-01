package com.alibaba.fastjson.serializer;

import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Type;

public class RectangleSerializer implements ObjectSerializer {

    public final static RectangleSerializer instance = new RectangleSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Rectangle rectangle = (Rectangle) object;
        if (rectangle == null) {
            out.writeNull();
            return;
        }
        
        char sep = '{';
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            out.write('{');
            out.writeFieldName("@type");
            out.writeString(Rectangle.class.getName());
            sep = ',';
        }
        
        out.writeFieldValue(sep, "x", rectangle.getX());
        out.writeFieldValue(',', "y", rectangle.getY());
        out.writeFieldValue(',', "width", rectangle.getWidth());
        out.writeFieldValue(',', "height", rectangle.getHeight());
        out.write('}');

    }

}
