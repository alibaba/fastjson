package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;

public class TimeDeserializer implements ObjectDeserializer {

    public final static TimeDeserializer instance = new TimeDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        Object val = parser.parse();

        if (val == null) {
            return null;
        }

        if (val instanceof java.sql.Time) {
            return (T) val;
        } else if (val instanceof Number) {
            return (T) new java.sql.Time(((Number) val).longValue());
        } else if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }
            
            JSONScanner dateLexer = new JSONScanner(strVal);
            if (dateLexer.scanISO8601DateIfMatch()) {
                return (T) dateLexer.getCalendar().getTime();
            }

            long longVal = Long.parseLong(strVal);
            return (T) new java.sql.Time(longVal);
        }
        
        throw new JSONException("parse error");
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
