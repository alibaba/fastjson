package com.alibaba.fastjson;

import java.io.IOException;
import java.io.Writer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class JSONWriter {

    private SerializeWriter   writer;
    private final Writer      out;

    private JSONSerializer    serializer;

    private JSONStreamContext context;

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

    @Deprecated
    public void writeStartObject() {
        startObject();
    }

    @Deprecated
    public void writeEndObject() {
        endObject();
    }

    @Deprecated
    public void writeStartArray() {
        startArray();
    }

    @Deprecated
    public void writeEndArray() {
        endArray();
    }

    public void startObject() {
        if (context != null) {
            beginStructure();
        }
        context = new JSONStreamContext(context, JSONStreamState.BeginObject);
        writer.write('{');
    }

    public void endObject() {
        writer.write('}');
        endStructure();
    }

    public void writeKey(String key) {
        if (context.getState() == JSONStreamState.PropertyValue) {
            writer.write(',');
        }
        writer.writeString(key);
        context.setState(JSONStreamState.PropertyKey);
    }

    public void writeValue(Object object) {
        switch (context.getState()) {
            case PropertyKey:
                writer.write(':');
                break;
            case ArrayValue:
                writer.write(',');
                break;
            default:
                break;
        }

        serializer.write(object);
        context.setState(JSONStreamState.PropertyValue);
    }

    public void startArray() {
        if (context != null) {
            beginStructure();
        }

        context = new JSONStreamContext(context, JSONStreamState.BeginArray);
        writer.write('[');
    }

    private void beginStructure() {
        final JSONStreamState state = context.getState();
        switch (state) {
            case PropertyKey:
                writer.write(':');
                break;
            case ArrayValue:
                writer.write(',');
                break;
            case BeginObject:
                break;
            case BeginArray:
                break;
            default:
                throw new JSONException("illegal state : " + state);
        }
    }

    public void endArray() {
        writer.write(']');
        endStructure();
    }

    private void endStructure() {
        context = context.getParent();

        if (context == null) {
            // skip
        } else {
            final JSONStreamState state = context.getState();
            JSONStreamState newState = null;
            switch (state) {
                case PropertyKey:
                    newState = JSONStreamState.PropertyValue;
                    break;
                case BeginArray:
                    newState = JSONStreamState.ArrayValue;
                    break;
                case ArrayValue:
                    break;
                default:
                    break;
            }
            if (newState != null) {
                context.setState(newState);
            }
        }
    }
}
