package com.alibaba.fastjson;

import java.io.IOException;
import java.io.Writer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class JSONWriter {

    private SerializeWriter writer;
    private final Writer    out;

    private JSONSerializer  serializer;

    private Context         context;

    public static enum State {
        BeginObject, //
        PropertyKey, //
        PropertyValue, //
        BeginArray, //
        ArrayValue
    }

    public static class Context {

        private final Context parent;

        private State         state;

        public Context(Context parent, State state){
            this.parent = parent;
            this.state = state;
        }

        public Context getParent() {
            return parent;
        }

        public State getState() {
            return state;
        }

        public void setState(State state) {
            this.state = state;
        }

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
        if (context == null) {
            context = new Context(null, State.BeginObject);
        } else {
            if (context.getState() == State.PropertyKey) {
                writer.write(':');
            } else if (context.getState() == State.ArrayValue) {
                writer.write(',');
            } else if (context.getState() == State.BeginObject) {
                // skip
            } else if (context.getState() == State.BeginArray) {
                // skip
            } else {
                throw new JSONException("illegal state : " + context.getState());
            }
            context = new Context(context, State.BeginObject);
        }
        writer.write('{');
    }

    public void writeEndObject() {
        writer.write('}');
        context = context.getParent();
        if (context == null) {
            // skip
        } else if (context.getState() == State.PropertyKey) {
            context.setState(State.PropertyValue);
        } else if (context.getState() == State.BeginArray) {
            context.setState(State.ArrayValue);
        } else if (context.getState() == State.ArrayValue) {
            // skip
        }
    }

    public void writeKey(String key) {
        if (context.getState() == State.PropertyValue) {
            writer.write(',');
        }
        writer.writeString(key);
        context.setState(State.PropertyKey);
    }

    public void writeValue(Object object) {
        if (context.getState() == State.PropertyKey) {
            writer.write(':');
        }
        serializer.write(object);
        context.setState(State.PropertyValue);
    }

    public void writeStartArray() {
        if (context == null) {
            context = new Context(null, State.BeginArray);
        } else {
            if (context.getState() == State.PropertyKey) {
                writer.write(':');
            } else if (context.getState() == State.ArrayValue) {
                writer.write(',');
            } else if (context.getState() == State.BeginArray) {
                // skipe
            } else {
                throw new JSONException("illegal state : " + context.getState());
            }
            context = new Context(context, State.BeginArray);
        }
        writer.write('[');
    }

    public void writeEndArray() {
        writer.write(']');
        context = context.getParent();

        if (context == null) {
            // skip
        } else if (context.getState() == State.PropertyKey) {
            context.setState(State.PropertyValue);
        } else if (context.getState() == State.BeginArray) {
            context.setState(State.ArrayValue);
        } else if (context.getState() == State.ArrayValue) {
            // skip
        }
    }
}
