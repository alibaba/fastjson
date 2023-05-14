package com.alibaba.json.test.performance;

import java.io.OutputStream;
import java.util.Collection;

import com.alibaba.json.test.codec.Codec;
import com.fasterxml.jackson.core.JsonFactory;

public class JacksonGroupDecoder implements Codec {

    private JsonFactory f = new JsonFactory();

    public String getName() {
        return "jackson-s";
    }

    public <T> T decodeObject(String text, Class<T> clazz) throws Exception {
        JacksonGroupParser parser = new JacksonGroupParser(f.createJsonParser(text));
        return (T) parser.parseGroup();
    }

    public Object decodeObject(String text) throws Exception {
        JacksonGroupParser parser = new JacksonGroupParser(f.createJsonParser(text));
        return parser.parseGroup();
    }

    public Object decode(String text) throws Exception {
        JacksonGroupParser parser = new JacksonGroupParser(f.createJsonParser(text));
        return parser.parseGroup();
    }

    public <T> Collection<T> decodeArray(String text, Class<T> clazz) throws Exception {
        throw new UnsupportedOperationException();
    }

    public String encode(Object object) throws Exception {
        throw new UnsupportedOperationException();
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
        // TODO Auto-generated method stub
        
    }
}
