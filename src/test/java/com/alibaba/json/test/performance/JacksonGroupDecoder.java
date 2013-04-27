package com.alibaba.json.test.performance;

import java.util.Collection;

import org.codehaus.jackson.JsonFactory;

import com.alibaba.json.test.codec.Codec;

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
}
