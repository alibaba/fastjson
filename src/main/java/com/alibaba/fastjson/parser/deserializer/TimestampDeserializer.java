package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Date;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;

public class TimestampDeserializer implements ObjectDeserializer {

    public final static TimestampDeserializer instance = new TimestampDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultExtJSONParser parser, Type clazz) {
        Object val = parser.parse();
        
        if (val == null) {
            return null;
        }

        if (val instanceof java.util.Date) {
            return (T) new java.sql.Timestamp(((Date) val).getTime());
        }

        if (val instanceof Number) {
            return (T) new java.sql.Timestamp(((Number) val).longValue());
        }

        if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }

            long longVal = Long.parseLong(strVal);
            return (T) new java.sql.Timestamp(longVal);
        }

        throw new JSONException("parse error");
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
