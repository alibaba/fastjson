package com.alibaba.json.test.codec;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;

public class FastjsonManualCodec implements Codec {

    private ParserConfig    config = new ParserConfig();
    private SerializeConfig serializeConfig = new SerializeConfig();

    public FastjsonManualCodec(){
        System.out.println("fastjson-" + JSON.VERSION);    
        
        serializeConfig.put(MediaContent.class, new MediaContentSerializer());
    }

    public String getName() {
        return "fastjson-manual";
    }

    public <T> T decodeObject(String text, Class<T> clazz) {
        DefaultJSONParser parser = new DefaultJSONParser(text, config);
        parser.config(Feature.DisableCircularReferenceDetect, true);
        return parser.parseObject(clazz);
    }

    public <T> Collection<T> decodeArray(String text, Class<T> clazz) throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser(text, config);
        parser.config(Feature.DisableCircularReferenceDetect, true);
        return parser.parseArray(clazz);
    }

    public final Object decodeObject(String text) {
        DefaultJSONParser parser = new DefaultJSONParser(text, config);
        parser.config(Feature.DisableCircularReferenceDetect, true);
        return parser.parse();
    }

    public final Object decode(String text) {
        DefaultJSONParser parser = new DefaultJSONParser(text, config);
        parser.config(Feature.DisableCircularReferenceDetect, true);
        return parser.parse();
    }

    // private JavaBeanSerializer serializer = new JavaBeanSerializer(Long_100_Entity.class);

    public String encode(Object object) throws Exception {
        SerializeWriter out = new SerializeWriter();
        out.config(SerializerFeature.DisableCircularReferenceDetect, true);

        JSONSerializer serializer = new JSONSerializer(out, serializeConfig);
        serializer.write(object);

        String text = out.toString();

        out.close();

        return text;
    }

    @SuppressWarnings("unchecked")
    public <T> T decodeObject(byte[] input, Class<T> clazz) throws Exception {
        return (T) JSON.parseObject(input, clazz, Feature.DisableCircularReferenceDetect);
    }

    public static class ImageSerializer implements ObjectSerializer {

        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                          int features) throws IOException {
            Image image = (Image) object;
            
            SerializeWriter out = serializer.out;
            
            out.writeFieldValue('{', "height", image.getHeight());
            out.writeFieldValueStringWithDoubleQuote(',', "size", image.getSize().name());
            
            String tile = image.getTitle();
            out.writeFieldValueStringWithDoubleQuoteCheck(',', "title", tile);
            out.writeFieldValueStringWithDoubleQuoteCheck(',', "uri", image.getUri());
            out.writeFieldValue(',', "width", image.getWidth());

            out.write('}');
            // TODO
        }
        
    }

    public static class MediaSerializer implements ObjectSerializer {

        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                          int features) throws IOException {
            Media image = (Media) object;
            
            SerializeWriter out = serializer.out;
            out.writeFieldValue('{', "bitrate", image.getBitrate());
            out.writeFieldValue(',', "duration", image.getDuration());
            out.writeFieldValue(',', "height", image.getHeight());
            
            String format = image.getFormat();
            out.writeFieldValueStringWithDoubleQuoteCheck(',', "format", format);
            
            out.writeFieldValue(',', "size", image.getSize());
            out.writeFieldValue(',', "height", image.getHeight());
            
            List<String> persons = image.getPersons();
            out.writeFieldName("persons");
            out.write(persons);
            out.writeFieldValueStringWithDoubleQuote(',', "player", image.getPlayer().name());
            out.writeFieldValue(',', "size", image.getSize());
            
            String title = image.getTitle();
            out.writeFieldValue(',', "title", title);
            
            String uri = image.getUri();
            out.writeFieldValueStringWithDoubleQuoteCheck(',', "uri", uri);
            
            out.writeFieldValue(',', "width", image.getWidth());
            
            out.write('}');
        }
        
    }
    
    public static class MediaContentSerializer implements ObjectSerializer {
        MediaSerializer mediaSer = new MediaSerializer();
        ImageSerializer imageSer = new ImageSerializer();

        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                          int features) throws IOException {
            MediaContent mediaContent = (MediaContent) object;
            
            SerializeWriter out = serializer.out;
            
            out.write('{');
            
            out.writeFieldName("image", false);
            List<Image> images = mediaContent.images;
            
            out.write('[');
            for (int i = 0, size = images.size(); i < size; ++i) {
                if (i != 0) {
                    out.write(',');
                }
                Image image = images.get(i);
                imageSer.write(serializer, image, Integer.valueOf(i), Image.class, features);
            }
            out.write(']');
            out.write(',');
            
            out.writeFieldName("media", false);
            mediaSer.write(serializer, mediaContent.media, "media", Media.class, features);
            
            out.write('}');
        }
        
    }

    @Override
    public byte[] encodeToBytes(Object object) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void encode(OutputStream out, Object object) throws Exception {
        out.write(encodeToBytes(object));        
    }
}
