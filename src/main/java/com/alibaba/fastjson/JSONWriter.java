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

    public void writeStartObject() {
        if (context == null) {
            context = new JSONStreamContext(null, JSONStreamState.BeginObject);
        } else {
            if (context.getState() == JSONStreamState.PropertyKey) {
                writer.write(':');
            } else if (context.getState() == JSONStreamState.ArrayValue) {
                writer.write(',');
            } else if (context.getState() == JSONStreamState.BeginObject) {
                // skip
            } else if (context.getState() == JSONStreamState.BeginArray) {
                // skip
            } else {
                throw new JSONException("illegal state : " + context.getState());
            }
            context = new JSONStreamContext(context, JSONStreamState.BeginObject);
        }
        writer.write('{');
    }

    public void writeEndObject() {
        writer.write('}');
        context = context.getParent();
        if (context == null) {
            // skip
        } else if (context.getState() == JSONStreamState.PropertyKey) {
            context.setState(JSONStreamState.PropertyValue);
        } else if (context.getState() == JSONStreamState.BeginArray) {
            context.setState(JSONStreamState.ArrayValue);
        } else if (context.getState() == JSONStreamState.ArrayValue) {
            // skip
        }
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

    public void writeStartArray() {
        if (context == null) {
            context = new JSONStreamContext(null, JSONStreamState.BeginArray);
        } else {
            if (context.getState() == JSONStreamState.PropertyKey) {
                writer.write(':');
            } else if (context.getState() == JSONStreamState.ArrayValue) {
                writer.write(',');
            } else if (context.getState() == JSONStreamState.BeginArray) {
                // skipe
            } else {
                throw new JSONException("illegal state : " + context.getState());
            }
            context = new JSONStreamContext(context, JSONStreamState.BeginArray);
        }
        writer.write('[');
    }

    public void writeEndArray() {
        writer.write(']');
        context = context.getParent();

        if (context == null) {
            // skip
        } else if (context.getState() == JSONStreamState.PropertyKey) {
            context.setState(JSONStreamState.PropertyValue);
        } else if (context.getState() == JSONStreamState.BeginArray) {
            context.setState(JSONStreamState.ArrayValue);
        } else if (context.getState() == JSONStreamState.ArrayValue) {
            // skip
        }
    }
}
