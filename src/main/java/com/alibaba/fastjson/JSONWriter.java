package com.alibaba.fastjson;

import java.io.IOException;
import java.io.Writer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class JSONWriter {

    private SerializeWriter writer;
    private final Writer    out;

    private Stat            stat = Stat.None;
    private JSONSerializer  serializer;

    public static enum Stat {
        None, BeginObject, Key, Value
    }
    
    public static class Context {
        
    }

    public JSONWriter(Writer out){
        this.out = out;
        writer = new SerializeWriter();
        serializer = new JSONSerializer(writer);
    }

    public void flush() throws IOException {
        writer.writeTo(out);
        writer = new SerializeWriter();
        serializer = new JSONSerializer(writer);
    }

    public void close() throws IOException {
        if (writer.size() != 0) {
            flush();
        }
    }

    public void writeStartObject() {
        writer.write('{');
        stat = Stat.None;
    }

    public void writeKey(String key) {
        if (stat == Stat.Value) {
            writer.write(',');
        }
        writer.writeString(key);
    }

    public void writeValue(Object object) {
        serializer.write(object);
    }

    public void writeEndObject() {
        writer.write('}');
    }
}
