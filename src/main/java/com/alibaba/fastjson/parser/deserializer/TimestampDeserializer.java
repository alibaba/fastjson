package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;

public class TimestampDeserializer extends AbstractDateDeserializer implements ObjectDeserializer {

    public final static TimestampDeserializer instance = new TimestampDeserializer();

    @SuppressWarnings("unchecked")
    protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {

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

            DateFormat dateFormat = parser.getDateFormat();
            try {
                Date date = (Date) dateFormat.parse(strVal);
                return (T) new Timestamp(date.getTime());
            } catch (ParseException e) {
                // skip
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
