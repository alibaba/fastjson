package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;

@SuppressWarnings("rawtypes")
public class EnumDeserializer implements ObjectDeserializer {

    private final Class<?>           enumClass;

    private final Map<Integer, Enum> ordinalMap = new HashMap<Integer, Enum>();
    private final Map<String, Enum>  nameMap    = new HashMap<String, Enum>();

    public EnumDeserializer(Class<?> enumClass){
        this.enumClass = enumClass;

        try {
            Method valueMethod = enumClass.getMethod("values");
            Object[] values = (Object[]) valueMethod.invoke(null);
            for (Object value : values) {
                Enum e = (Enum) value;
                ordinalMap.put(e.ordinal(), e);
                nameMap.put(e.name(), e);
            }
        } catch (Exception ex) {
            throw new JSONException("init enum values error, " + enumClass.getName());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        try {
            Object value;
            final JSONLexer lexer = parser.getLexer();
            if (lexer.token() == JSONToken.LITERAL_INT) {
                value = lexer.intValue();
                lexer.nextToken(JSONToken.COMMA);

                T e = (T) ordinalMap.get(value);
                if (e == null) {
                    throw new JSONException("parse enum " + enumClass.getName() + " error, value : " + value);
                }
                return e;
            } else if (lexer.token() == JSONToken.LITERAL_STRING) {
                String strVal = lexer.stringVal();
                lexer.nextToken(JSONToken.COMMA);

                if (strVal.length() == 0) {
                    return (T) null;
                }

                value = nameMap.get(strVal);

                return (T) Enum.valueOf((Class<Enum>) enumClass, strVal);
            } else if (lexer.token() == JSONToken.NULL) {
                value = null;
                lexer.nextToken(JSONToken.COMMA);

                return null;
            } else {
                value = parser.parse();
            }

            throw new JSONException("parse enum " + enumClass.getName() + " error, value : " + value);
        } catch (JSONException e) {
            throw e;
        } catch (Throwable e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
