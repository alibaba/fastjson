package com.alibaba.json.test.codec;

import java.io.OutputStream;
import java.util.Collection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastjsonSCodec implements Codec {
    public FastjsonSCodec(){
        System.out.println("fastjson-" + JSON.VERSION);    
    }

    public String getName() {
        return "fastjsonS";
    }

    public <T> T decodeObject(String text, Class<T> clazz) {
        ParserConfig    config = new ParserConfig();
        DefaultJSONParser parser = new DefaultJSONParser(text, config);
        parser.config(Feature.DisableCircularReferenceDetect, true);
        return parser.parseObject(clazz);
    }

    public <T> Collection<T> decodeArray(String text, Class<T> clazz) throws Exception {
        ParserConfig    config = new ParserConfig();
        DefaultJSONParser parser = new DefaultJSONParser(text, config);
        parser.config(Feature.DisableCircularReferenceDetect, true);
        return parser.parseArray(clazz);
    }

    public final Object decodeObject(String text) {
        ParserConfig    config = new ParserConfig();
        DefaultJSONParser parser = new DefaultJSONParser(text, config);
        parser.config(Feature.DisableCircularReferenceDetect, true);
        return parser.parse();
    }

    public final Object decode(String text) {
        ParserConfig    config = new ParserConfig();
        DefaultJSONParser parser = new DefaultJSONParser(text, config);
        parser.config(Feature.DisableCircularReferenceDetect, true);
        return parser.parse();
    }

    // private JavaBeanSerializer serializer = new JavaBeanSerializer(Long_100_Entity.class);

    public String encode(Object object) throws Exception {
        SerializeConfig config = new SerializeConfig();
        SerializeWriter out = new SerializeWriter();
        out.config(SerializerFeature.DisableCircularReferenceDetect, true);
//        out.config(SerializerFeature.DisableCheckSpecialChar, true);

        JSONSerializer serializer = new JSONSerializer(out, config);
        serializer.write(object);

        String text = out.toString();

        out.close();

        return text;
    }

    @SuppressWarnings("unchecked")
    public <T> T decodeObject(byte[] input, Class<T> clazz) throws Exception {
        return (T) JSON.parseObject(input, clazz, Feature.DisableCircularReferenceDetect);
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
