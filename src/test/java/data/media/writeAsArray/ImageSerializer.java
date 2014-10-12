package data.media.writeAsArray;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import data.media.Image;

public class ImageSerializer implements ObjectSerializer {

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        Image image = (Image) object;

        SerializeWriter out = serializer.getWriter();
        out.write('[');
        
        out.writeIntAndChar(image.getHeight(), ',');
        out.writeIntAndChar(image.getWidth(), ',');
        out.writeString(image.getSize().name(), ',');
        out.writeString(image.getTitle(), ',');
        out.writeString(image.getUri());
        
        out.write(']');
    }

}
