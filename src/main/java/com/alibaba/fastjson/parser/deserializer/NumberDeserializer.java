package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.util.TypeUtils;

public class NumberDeserializer implements ObjectDeserializer {

    public final static NumberDeserializer instance = new NumberDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        final JSONLexer lexer = parser.lexer;
        if (lexer.token() == JSONToken.LITERAL_INT) {
            if (clazz == double.class || clazz  == Double.class) {
                String val = lexer.numberString();
                lexer.nextToken(JSONToken.COMMA);
                return (T) Double.valueOf(Double.parseDouble(val));
            }
            
            long val = lexer.longValue();
            lexer.nextToken(JSONToken.COMMA);

            if (clazz == short.class || clazz == Short.class) {
                if (val > Short.MAX_VALUE || val < Short.MIN_VALUE) {
                    throw new JSONException("short overflow : " + val);
                }
                return (T) Short.valueOf((short) val);
            }

            if (clazz == byte.class || clazz == Byte.class) {
                if (val > Byte.MAX_VALUE || val < Byte.MIN_VALUE) {
                    throw new JSONException("short overflow : " + val);
                }

                return (T) Byte.valueOf((byte) val);
            }

            if (val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE) {
                return (T) Integer.valueOf((int) val);
            }
            return (T) Long.valueOf(val);
        }

        if (lexer.token() == JSONToken.LITERAL_FLOAT) {
            if (clazz == double.class || clazz == Double.class) {
                String val = lexer.numberString();
                lexer.nextToken(JSONToken.COMMA);
                return (T) Double.valueOf(Double.parseDouble(val));
            }

            BigDecimal val = lexer.decimalValue();
            lexer.nextToken(JSONToken.COMMA);

            if (clazz == short.class || clazz == Short.class) {
                return (T) Short.valueOf(val.shortValue());
            }

            if (clazz == byte.class || clazz == Byte.class) {
                return (T) Byte.valueOf(val.byteValue());
            }

            return (T) val;
        }

        Object value = parser.parse();

        if (value == null) {
            return null;
        }

        if (clazz == double.class || clazz == Double.class) {
            return (T) TypeUtils.castToDouble(value);
        }

        if (clazz == short.class || clazz == Short.class) {
            return (T) TypeUtils.castToShort(value);
        }

        if (clazz == byte.class || clazz == Byte.class) {
            return (T) TypeUtils.castToByte(value);
        }

        return (T) TypeUtils.castToBigDecimal(value);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
