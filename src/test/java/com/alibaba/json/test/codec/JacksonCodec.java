package com.alibaba.json.test.codec;

import java.util.Collection;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;

public class JacksonCodec implements Codec {

    private ObjectMapper mapper = new ObjectMapper();

    public String getName() {
        return "jackson ";
    }

    public final <T> T decodeObject(String text, Class<T> clazz) {
        try {
            return mapper.readValue(text, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    

    public <T> T decodeObject(byte[] input, Class<T> clazz) throws Exception {
        try {
            return mapper.readValue(input, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <T> Collection<T> decodeArray(String text, Class<T> clazz) throws Exception {
        try {
            return (Collection<T>) mapper.readValue(text, new TypeReference<T>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Object decodeObject(String text) {
        try {
            return (ObjectNode) mapper.readTree(text);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Object decode(String text) {
        try {
            return mapper.readTree(text);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String encode(Object object) throws Exception {
        return mapper.writeValueAsString(object);
    }
}
