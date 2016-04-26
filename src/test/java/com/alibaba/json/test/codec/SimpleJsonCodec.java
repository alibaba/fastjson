package com.alibaba.json.test.codec;

import java.io.OutputStream;
import java.util.Collection;

import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

public class SimpleJsonCodec implements Codec {

    private JSONParser parser = new JSONParser();

    public String getName() {
        return "simplejson";
    }

    public <T> T decodeObject(String text, Class<T> clazz) throws Exception {
        return (T) parser.parse(text);
    }

    public <T> Collection<T> decodeArray(String text, Class<T> clazz) throws Exception {
        return (Collection<T>) parser.parse(text);
    }

    public Object decodeObject(String text) throws Exception {
        return parser.parse(text);
    }

    public Object decode(String text) throws Exception {
        return parser.parse(text);
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
