package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Arrays;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;

@SuppressWarnings("rawtypes")
public class EnumDeserializer implements ObjectDeserializer {

    private final Class<?> enumClass;
    protected final Enum[] enums;

    protected final Enum[] ordinalEnums;

    protected long[] enumNameHashCodes;

    public EnumDeserializer(Class<?> enumClass){
        this.enumClass = enumClass;

        ordinalEnums = (Enum[]) enumClass.getEnumConstants();

        long[] enumNameHashCodes = new long[ordinalEnums.length];
        this.enumNameHashCodes = new long[ordinalEnums.length];
        for (int i = 0; i < ordinalEnums.length; ++i) {
            String name = ordinalEnums[i].name();
            long hash = 0x811c9dc5;
            for (int j = 0; j < name.length(); ++j) {
                char ch = name.charAt(j);
                hash ^= ch;
                hash *= 0x1000193;
            }
            enumNameHashCodes[i] = hash;
            this.enumNameHashCodes[i] = hash;
        }

        Arrays.sort(this.enumNameHashCodes);

        this.enums = new Enum[ordinalEnums.length];
        for (int i = 0; i < this.enumNameHashCodes.length; ++i) {
            for (int j = 0; j < enumNameHashCodes.length; ++j) {
                if (this.enumNameHashCodes[i] == enumNameHashCodes[j]) {
                    this.enums[i] = ordinalEnums[j];
                    break;
                }
            }
        }
    }

    public Enum getEnumByHashCode(long hashCode) {
        if (enums == null) {
            return null;
        }

        int enumIndex = Arrays.binarySearch(this.enumNameHashCodes, hashCode);

        if (enumIndex < 0) {
            return null;
        }

        return enums[enumIndex];
    }
    
    public Enum<?> valueOf(int ordinal) {
        return ordinalEnums[ordinal];
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        try {
            Object value;
            final JSONLexer lexer = parser.lexer;
            final int token = lexer.token();
            if (token == JSONToken.LITERAL_INT) {
                int intValue = lexer.intValue();
                lexer.nextToken(JSONToken.COMMA);

                if (intValue < 0 || intValue > ordinalEnums.length) {
                    throw new JSONException("parse enum " + enumClass.getName() + " error, value : " + intValue);
                }

                return (T) ordinalEnums[intValue];
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

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
