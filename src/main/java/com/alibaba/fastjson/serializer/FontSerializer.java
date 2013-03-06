package com.alibaba.fastjson.serializer;

import java.awt.Font;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

import com.alibaba.fastjson.JSON;

public class FontSerializer implements AutowiredObjectSerializer {

    public final static FontSerializer instance = new FontSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Font font = (Font) object;
        if (font == null) {
            out.writeNull();
            return;
        }
        
        char sep = '{';
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            out.write('{');
            out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
            out.writeString(Font.class.getName());
            sep = ',';
        }
        
        out.writeFieldValue(sep, "name", font.getName());
        out.writeFieldValue(',', "style", font.getStyle());
        out.writeFieldValue(',', "size", font.getSize());
        out.write('}');

    }

    public Set<Type> getAutowiredFor() {
        return Collections.<Type>singleton(Font.class);
    }
}
