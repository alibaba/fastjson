package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.util.TimeZone;

public class TimeZoneSerializer implements ObjectSerializer {

    public final static TimeZoneSerializer instance = new TimeZoneSerializer();

    public void write(JSONSerializer serializer, Object object) throws IOException {
        if (object == null) {
            serializer.writeNull();
            return;
        }

        TimeZone timeZone = (TimeZone) object;
        serializer.write(timeZone.getID());
    }

}
