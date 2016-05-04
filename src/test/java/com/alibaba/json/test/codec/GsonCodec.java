package com.alibaba.json.test.codec;

import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GsonCodec implements Codec {

    private Gson gson = new Gson();

    public String getName() {
        return "gson";
    }

    public <T> T decodeObject(String text, Class<T> clazz) throws Exception {
        return (T) gson.fromJson(text, clazz);
    }

    public <T> Collection<T> decodeArray(String text, Class<T> clazz) throws Exception {
        return (Collection<T>) gson.fromJson(text, clazz);
    }

    public Object decodeObject(String text) throws Exception {
        return gson.fromJson(text, HashMap.class);
    }

    public Object decode(String text) throws Exception {
        return gson.fromJson(text, JsonObject.class);
    }

    public String encode(Object object) throws Exception {
        return gson.toJson(object);
    }

    public <T> T decodeObject(byte[] input, Class<T> clazz) throws Exception {
        throw new UnsupportedOperationException();
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
