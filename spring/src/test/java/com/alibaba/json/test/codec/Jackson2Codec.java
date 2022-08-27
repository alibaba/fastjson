package com.alibaba.json.test.codec;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class Jackson2Codec implements Codec {

    private ObjectMapper mapper = new ObjectMapper();

    public String getName() {
        return "jackson2";
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

    @Override
    public byte[] encodeToBytes(Object object) throws Exception {
        return mapper.writeValueAsBytes(object);
    }

    @Override
    public void encode(OutputStream out, Object object) throws Exception {
        Class<?> clazz = object.getClass();
        JsonGenerator generator = constructGenerator(out);
        JavaType type = mapper.getTypeFactory().constructType(clazz);
        ObjectWriter writer = mapper.writerFor(type);
        writer.writeValue(generator, object);
        generator.flush();
        generator.close();
    }
    
    protected final JsonGenerator constructGenerator(OutputStream out) throws IOException {
        return mapper.getFactory().createGenerator(out, JsonEncoding.UTF8);
    }
}
