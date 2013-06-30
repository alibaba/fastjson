package com.alibaba.fastjson.serializer;

import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;

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
            out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
            out.writeString(Point.class.getName());
            sep = ',';
        }
        
        out.writeFieldValue(sep, "x", font.getX());
        out.writeFieldValue(',', "y", font.getY());
        out.write('}');

    }

}
