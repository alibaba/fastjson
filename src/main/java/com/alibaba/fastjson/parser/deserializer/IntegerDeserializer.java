package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.util.TypeUtils;

public class IntegerDeserializer implements ObjectDeserializer {
    public final static IntegerDeserializer instance = new IntegerDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        return (T) deserialze(parser);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T deserialze(DefaultJSONParser parser) {
        final JSONLexer lexer = parser.getLexer();
        
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }
        
        if (lexer.token() == JSONToken.LITERAL_INT) {
            int val = lexer.intValue();
            lexer.nextToken(JSONToken.COMMA);
            return (T) Integer.valueOf(val);
        }
        
        if (lexer.token() == JSONToken.LITERAL_FLOAT) {
            BigDecimal decimalValue = lexer.decimalValue();
            lexer.nextToken(JSONToken.COMMA);
            return (T) Integer.valueOf(decimalValue.intValue());
        }
        
        Object value = parser.parse();

        return (T) TypeUtils.castToInt(value);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
