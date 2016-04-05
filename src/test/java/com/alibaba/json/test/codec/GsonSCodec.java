package com.alibaba.json.test.codec;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.Gson;

public class GsonSCodec implements Codec {

    public String getName() {
        return "gsonS";
    }

    // gson 模拟首次
    public <T> T decodeObject(String text, Type clazz) {
        Gson gson = new Gson(); // 每次new Gson模拟首次
        return gson.fromJson(text, clazz);
    }
    
    public String encode(Object object) throws Exception {
        Gson gson = new Gson(); // 每次new Gson模拟首次
        return gson.toJson(object);
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

   

    @SuppressWarnings("unchecked")
    public <T> T decodeObject(byte[] input, Type clazz) throws Exception {
        throw new RuntimeException("TODO");
    }

}
