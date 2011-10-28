package com.alibaba.fastjson.serializer;

import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Type;

public class PointSerializer implements ObjectSerializer {

    public final static PointSerializer instance = new PointSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Point font = (Point) object;
        if (font == null) {
            out.writeNull();
            return;
        }
        
        char sep = '{';
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            out.write('{');
            out.writeFieldName("@type");
            out.writeString(Point.class.getName());
            sep = ',';
        }
        
        out.writeFieldValue(sep, "x", font.getX());
        out.writeFieldValue(',', "y", font.getY());
        out.write('}');

    }

}
