package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;

public class HashMapDeserializer implements ObjectDeserializer {

    public final static HashMapDeserializer instance = new HashMapDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONLexer lexer = parser.getLexer();
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }

        Map<Object, Object> map = createMap();

        ParseContext context = parser.getContext();

        try {
            parser.setContext(context, map, fieldName);

            if (lexer.token() == JSONToken.RBRACE) {
                lexer.nextToken(JSONToken.COMMA);
                return (T) map;
            }

            deserialze(parser, type, fieldName, map);
        } finally {
            parser.setContext(context);
        }

        return (T) map;
    }

    protected void deserialze(DefaultJSONParser parser, Type type, Object fieldName, Map<Object, Object> map) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type keyType = parameterizedType.getActualTypeArguments()[0];
            Type valueType = parameterizedType.getActualTypeArguments()[1];

            DefaultObjectDeserializer.instance.parseMap(parser, map, keyType, valueType, fieldName);
        } else {
            parser.parseObject(map, fieldName);
        }
    }

    protected Map<Object, Object> createMap() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        return map;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
