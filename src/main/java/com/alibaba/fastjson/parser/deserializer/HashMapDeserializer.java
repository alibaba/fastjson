package com.alibaba.fastjson.parser.deserializer;

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

        Map<String, Object> map = new HashMap<String, Object>();

        ParseContext context = parser.getContext();

        try {
            parser.setContext(context, map, fieldName);

            parser.parseObject(map);

        } finally {
            parser.setContext(context);
        }

        return (T) map;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
