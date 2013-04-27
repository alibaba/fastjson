package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.TimeZone;

public class TimeZoneSerializer implements ObjectSerializer {

    public final static TimeZoneSerializer instance = new TimeZoneSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.writeNull();
            return;
        }

        TimeZone timeZone = (TimeZone) object;
        serializer.write(timeZone.getID());
    }

}
