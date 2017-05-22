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
        
        out.writeInt(media.getBitrate());
        out.write(',');
        out.writeInt(media.getHeight());
        out.write(',');
        out.writeInt(media.getWidth());
        out.write(',');
        out.writeString(media.getCopyright(), ',');
        out.writeLong(media.getDuration());
        out.write(',');
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
        out.writeLong(media.getSize());
        out.write(',');
        out.writeString(media.getTitle(), ',');
        out.writeString(media.getUri(), ']');
    }

}