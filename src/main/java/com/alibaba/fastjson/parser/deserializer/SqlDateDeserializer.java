package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Date;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;

public class SqlDateDeserializer implements ObjectDeserializer {
    public final static SqlDateDeserializer instance = new SqlDateDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultExtJSONParser parser, Type clazz, Object fieldName) {
        Object val = parser.parse();
        if (val == null) {
            return null;
        }
        
        if (val instanceof java.util.Date) {
            val = new java.sql.Date(((Date) val).getTime());
        } else if (val instanceof Number) {
            val = (T) new java.sql.Date(((Number) val).longValue());
        } else if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }
            
            long longVal = Long.parseLong(strVal);
            return (T) new java.sql.Date(longVal);
        } else {
            throw new JSONException("parse error : " + val);
        }

        return (T) val;
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
