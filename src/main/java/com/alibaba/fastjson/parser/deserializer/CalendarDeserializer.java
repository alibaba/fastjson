package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;


public class CalendarDeserializer implements ObjectDeserializer {
    public static final CalendarDeserializer instance = new CalendarDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Date date = DateDeserializer.instance.deserialze(parser, type, fieldName);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        return (T) calendar;
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }

}
