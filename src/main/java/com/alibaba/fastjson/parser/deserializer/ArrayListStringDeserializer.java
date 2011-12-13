package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;

public class ArrayListStringDeserializer implements ObjectDeserializer {

    public final static ArrayListStringDeserializer instance = new ArrayListStringDeserializer();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {

        final JSONLexer lexer = parser.getLexer();
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }

        Collection array = null;

        if (type == Set.class || type == HashSet.class) {
            array = new HashSet();
        } else {
            if (type instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) type).getRawType();
                if (rawType == Set.class || rawType == HashSet.class) {
                    array = new HashSet();
                }
            }
        }

        if (array == null) {
            array = new ArrayList();
        }

        parseArray(parser, array);

        return (T) array;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void parseArray(DefaultJSONParser parser, Collection array) {
        JSONLexer lexer = parser.getLexer();

        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return;
        }

        if (lexer.token() == JSONToken.SET) {
            lexer.nextToken();
        }

        if (lexer.token() != JSONToken.LBRACKET) {
            throw new JSONException("exepct '[', but " + lexer.token());
        }

        lexer.nextToken(JSONToken.LITERAL_STRING);

        for (;;) {
            if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                while (lexer.token() == JSONToken.COMMA) {
                    lexer.nextToken();
                    continue;
                }
            }

            if (lexer.token() == JSONToken.RBRACKET) {
                break;
            }

            String value;
            if (lexer.token() == JSONToken.LITERAL_STRING) {
                value = lexer.stringVal();
                lexer.nextToken(JSONToken.COMMA);
            } else {
                Object obj = parser.parse();
                if (obj == null) {
                    value = null;
                } else {
                    value = obj.toString();
                }
            }

            array.add(value);

            if (lexer.token() == JSONToken.COMMA) {
                lexer.nextToken(JSONToken.LITERAL_STRING);
                continue;
            }
        }

        lexer.nextToken(JSONToken.COMMA);
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }
}
