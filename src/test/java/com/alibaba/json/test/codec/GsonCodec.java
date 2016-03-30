package com.alibaba.json.test.codec;

import java.util.Collection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;

public class GsonCodec implements Codec {

    Gson gson = new Gson();

    public String getName() {
        return "gson";
    }

    public <T> T decodeObject(String text, Class<T> clazz) {
        return gson.fromJson(text, clazz);
    }

    public <T> Collection<T> decodeArray(String text, Class<T> clazz) throws Exception {
        throw new Exception("TODO");
    }

    public final Object decodeObject(String text) {
        throw new RuntimeException("TODO");
    }

    public final Object decode(String text) {
        throw new RuntimeException("TODO");
    }

    // private JavaBeanSerializer serializer = new JavaBeanSerializer(Long_100_Entity.class);

    public String encode(Object object) throws Exception {
        return gson.toJson(object);
    }

    @SuppressWarnings("unchecked")
    public <T> T decodeObject(byte[] input, Class<T> clazz) throws Exception {
        throw new RuntimeException("TODO");
    }

}
