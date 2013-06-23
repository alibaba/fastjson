package com.alibaba.fastjson;

import java.io.Closeable;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONReaderScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.util.TypeUtils;

public class JSONReader implements Closeable {

    private final DefaultJSONParser parser;
    private JSONStreamContext       context;

    public JSONReader(Reader reader){
        this(new DefaultJSONParser(new JSONReaderScanner(reader)));
    }

    public JSONReader(JSONLexer lexer){
        this(new DefaultJSONParser(lexer));
    }

    public JSONReader(DefaultJSONParser parser){
        this.parser = parser;
    }

    public void config(Feature feature, boolean state) {
        this.parser.config(feature, state);
    }

    public void startObject() {
        if (context == null) {
            context = new JSONStreamContext(null, JSONStreamState.BeginObject);
        } else {
            if (context.getState() == JSONStreamState.PropertyKey) {
                parser.accept(JSONToken.COLON);
            } else if (context.getState() == JSONStreamState.ArrayValue) {
                parser.accept(JSONToken.COMMA);
            } else if (context.getState() == JSONStreamState.BeginObject) {
                // skip
            } else if (context.getState() == JSONStreamState.BeginArray) {
                // skip
            } else {
                throw new JSONException("illegal state : " + context.getState());
            }
            context = new JSONStreamContext(context, JSONStreamState.BeginObject);
        }

        this.parser.accept(JSONToken.LBRACE, JSONToken.IDENTIFIER);
    }

    public void endObject() {
        this.parser.accept(JSONToken.RBRACE);
        endStructure();
    }

    public void startArray() {
        if (context == null) {
            context = new JSONStreamContext(null, JSONStreamState.BeginArray);
        } else {
            if (context.getState() == JSONStreamState.PropertyKey) {
                parser.accept(JSONToken.COLON);
            } else if (context.getState() == JSONStreamState.ArrayValue) {
                parser.accept(JSONToken.COMMA);
            } else if (context.getState() == JSONStreamState.BeginArray) {
                // skipe
            } else {
                throw new JSONException("illegal state : " + context.getState());
            }
            context = new JSONStreamContext(context, JSONStreamState.BeginArray);
        }
        this.parser.accept(JSONToken.LBRACKET);
    }

    public void endArray() {
        this.parser.accept(JSONToken.RBRACKET);
        endStructure();
    }

    private void endStructure() {
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

    public boolean hasNext() {
        if (context == null) {
            throw new JSONException("context is null");
        }

        final int token = parser.getLexer().token();
        final JSONStreamState state = context.getState();
        switch (state) {
            case BeginArray:
            case ArrayValue:
                return token != JSONToken.RBRACKET;
            case BeginObject:
            case PropertyValue:
                return token != JSONToken.RBRACE;
            default:
                throw new JSONException("illegal state : " + state);
        }
    }

    public void close() {
        parser.close();
    }

    public Integer readInteger() {
        Object object;
        if (context == null) {
            object = parser.parse();
        } else {
            readBefore();
            object = parser.parse();
            readAfter();
        }

        return TypeUtils.castToInt(object);
    }

    public Long readLong() {
        Object object;
        if (context == null) {
            object = parser.parse();
        } else {
            readBefore();
            object = parser.parse();
            readAfter();
        }

        return TypeUtils.castToLong(object);
    }

    public String readString() {
        Object object;
        if (context == null) {
            object = parser.parse();
        } else {
            readBefore();
            object = parser.parse();
            readAfter();
        }

        return TypeUtils.castToString(object);
    }

    public <T> T readObject(Type type) {
        if (context == null) {
            return parser.parseObject(type);
        }

        readBefore();
        T object = parser.parseObject(type);
        readAfter();
        return object;
    }

    public <T> T readObject(Class<T> type) {
        if (context == null) {
            return parser.parseObject(type);
        }

        readBefore();
        T object = parser.parseObject(type);
        readAfter();
        return object;
    }

    public void readObject(Object object) {
        if (context == null) {
            parser.parseObject(object);
            return;
        }

        readBefore();
        parser.parseObject(object);
        readAfter();
    }

    public Object readObject() {
        if (context == null) {
            return parser.parse();
        }

        readBefore();
        Object object;
        switch (context.getState()) {
            case BeginObject:
            case PropertyValue:
                object = parser.parseKey();
                break;
            default:
                object = parser.parse();
                break;
        }

        readAfter();
        return object;
    }

    @SuppressWarnings("rawtypes")
    public Object readObject(Map object) {
        if (context == null) {
            return parser.parseObject(object);
        }

        readBefore();
        Object value = parser.parseObject(object);
        readAfter();
        return value;
    }

    private void readBefore() {
        JSONStreamState state = context.getState();
        // before
        switch (state) {
            case PropertyKey:
                parser.accept(JSONToken.COLON);
                break;
            case PropertyValue:
                parser.accept(JSONToken.COMMA, JSONToken.IDENTIFIER);
                break;
            case ArrayValue:
                parser.accept(JSONToken.COMMA);
                break;
            case BeginObject:
                break;
            case BeginArray:
                break;
            default:
                throw new JSONException("illegal state : " + state);
        }
    }

    private void readAfter() {
        JSONStreamState state = context.getState();
        JSONStreamState newStat = null;
        switch (state) {
            case BeginObject:
                newStat = JSONStreamState.PropertyKey;
                break;
            case PropertyKey:
                newStat = JSONStreamState.PropertyValue;
                break;
            case PropertyValue:
                newStat = JSONStreamState.PropertyKey;
                break;
            case ArrayValue:
                break;
            case BeginArray:
                newStat = JSONStreamState.ArrayValue;
                break;
            default:
                throw new JSONException("illegal state : " + state);
        }
        if (newStat != null) {
            context.setState(newStat);
        }
    }

}
