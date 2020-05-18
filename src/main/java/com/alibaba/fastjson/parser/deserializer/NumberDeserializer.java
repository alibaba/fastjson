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

            if (clazz == short.class || clazz == Short.class) {
                BigDecimal val = lexer.decimalValue();
                lexer.nextToken(JSONToken.COMMA);
                short shortValue = TypeUtils.shortValue(val);
                return (T) Short.valueOf(shortValue);
            }

            if (clazz == byte.class || clazz == Byte.class) {
                BigDecimal val = lexer.decimalValue();
                lexer.nextToken(JSONToken.COMMA);
                byte byteValue = TypeUtils.byteValue(val);
                return (T) Byte.valueOf(byteValue);
            }

            BigDecimal val = lexer.decimalValue();
            lexer.nextToken(JSONToken.COMMA);



            return (T) val;
        }

        if (lexer.token() == JSONToken.IDENTIFIER && "NaN".equals(lexer.stringVal())) {
            lexer.nextToken();
            Object nan = null;
            if (clazz == Double.class) {
                nan = Double.NaN;
            } else if (clazz == Float.class) {
                nan = Float.NaN;
            }
            return (T) nan;
        }

        Object value = parser.parse();

        if (value == null) {
            return null;
        }

        if (clazz == double.class || clazz == Double.class) {
            try {
                return (T) TypeUtils.castToDouble(value);
            } catch (Exception ex) {
                throw new JSONException("parseDouble error, field : " + fieldName, ex);
            }
        }

        if (clazz == short.class || clazz == Short.class) {
            try {
                return (T) TypeUtils.castToShort(value);
            } catch (Exception ex) {
                throw new JSONException("parseShort error, field : " + fieldName, ex);
            }
        }

        if (clazz == byte.class || clazz == Byte.class) {
            try {
                return (T) TypeUtils.castToByte(value);
            } catch (Exception ex) {
                throw new JSONException("parseByte error, field : " + fieldName, ex);
            }
        }

        return (T) TypeUtils.castToBigDecimal(value);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
