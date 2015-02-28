package data.media.writeAsArray;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import data.media.Media;

public class MediaSerializer implements ObjectSerializer {

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        Media media = (Media) object;

        SerializeWriter out = serializer.getWriter();
        out.write('[');
        
        out.writeIntAndChar(media.getBitrate(), ',');
        out.writeIntAndChar(media.getHeight(), ',');
        out.writeIntAndChar(media.getWidth(), ',');
        out.writeString(media.getCopyright(), ',');
        out.writeLongAndChar(media.getDuration(), ',');
        out.writeString(media.getFormat(), ',');
        out.write('[');
        for (int i = 0; i < media.getPersons().size(); ++i) {
            if(i != 0) {
                out.write(',');
            }
            out.writeString(media.getPersons().get(i));
        }
        out.write("],");
        out.writeString(media.getPlayer().name(), ',');
        out.writeLongAndChar(media.getSize(), ',');
        out.writeString(media.getTitle(), ',');
        out.writeString(media.getUri(), ']');
    }

}