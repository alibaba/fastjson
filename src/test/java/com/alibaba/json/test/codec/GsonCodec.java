package com.alibaba.json.test.codec;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.Gson;

public class GsonCodec implements Codec {

    Gson gson = new Gson();

    public String getName() {
        return "gson";
    }

    public <T> T decodeObject(String text, Type clazz) {
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

    public String encode(Object object) throws Exception {
        return gson.toJson(object);
    }

    @SuppressWarnings("unchecked")
    public <T> T decodeObject(byte[] input, Type clazz) throws Exception {
        throw new RuntimeException("TODO");
    }

}
