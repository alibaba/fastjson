package com.alibaba.json.test.codec;

import java.io.OutputStream;
import java.util.Collection;

import net.minidev.json.JSONValue;

public class JsonSmartCodec implements Codec {

    public String getName() {
        return "json-smart";
    }

    public <T> T decodeObject(String text, Class<T> clazz) throws Exception {
        throw new UnsupportedOperationException();
    }

    public <T> Collection<T> decodeArray(String text, Class<T> clazz) throws Exception {
        throw new UnsupportedOperationException();
    }

    public Object decodeObject(String text) throws Exception {
        Object value =  JSONValue.parse(text);
        
        return value;
    }

    public Object decode(String text) throws Exception {
        Object value = JSONValue.parse(text);
        
        return value;
    }

    public String encode(Object object) throws Exception {
        return JSONValue.toJSONString(object);
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
