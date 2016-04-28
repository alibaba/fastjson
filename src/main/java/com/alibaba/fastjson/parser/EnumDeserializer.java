package com.alibaba.fastjson.parser;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

@SuppressWarnings("rawtypes")
public class EnumDeserializer implements ObjectDeserializer {

    private final Class<?> enumClass;
    protected final Enum[]   values;

    public EnumDeserializer(Class<?> enumClass){
        this.enumClass = enumClass;
        values = (Enum[]) enumClass.getEnumConstants();
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        try {
            Object value;
            final JSONLexer lexer = parser.lexer;
            final int token = lexer.token;
            if (token == JSONToken.LITERAL_INT) {
                int intValue = lexer.intValue();
                lexer.nextToken(JSONToken.COMMA);

                if (intValue < 0 || intValue > values.length) {
                    throw new JSONException("parse enum " + enumClass.getName() + " error, value : " + intValue);
                }

                return (T) values[intValue];
            } else if (token == JSONToken.LITERAL_STRING) {
                String strVal = lexer.stringVal();
                lexer.nextToken(JSONToken.COMMA);

                if (strVal.length() == 0) {
                    return (T) null;
                }

                return (T) Enum.valueOf((Class<Enum>) enumClass, strVal);
            } else if (token == JSONToken.NULL) {
                value = null;
                lexer.nextToken(JSONToken.COMMA);

                return null;
            } else {
                value = parser.parse();
            }

            throw new JSONException("parse enum " + enumClass.getName() + " error, value : " + value);
        } catch (JSONException e) {
            throw e;
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }
}
