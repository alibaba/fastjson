package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.IOUtils;

public class CalendarCodec implements ObjectSerializer, ObjectDeserializer {

    public final static CalendarCodec instance = new CalendarCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
                                                                                                               throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        Calendar calendar = (Calendar) object;

        if (out.isEnabled(SerializerFeature.UseISO8601DateFormat)) {
            if (out.isEnabled(SerializerFeature.UseSingleQuotes)) {
                out.append('\'');
            } else {
                out.append('\"');
            }

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            int millis = calendar.get(Calendar.MILLISECOND);

            char[] buf;
            if (millis != 0) {
                buf = "0000-00-00T00:00:00.000".toCharArray();
                IOUtils.getChars(millis, 23, buf);
                IOUtils.getChars(second, 19, buf);
                IOUtils.getChars(minute, 16, buf);
                IOUtils.getChars(hour, 13, buf);
                IOUtils.getChars(day, 10, buf);
                IOUtils.getChars(month, 7, buf);
                IOUtils.getChars(year, 4, buf);

            } else {
                if (second == 0 && minute == 0 && hour == 0) {
                    buf = "0000-00-00".toCharArray();
                    IOUtils.getChars(day, 10, buf);
                    IOUtils.getChars(month, 7, buf);
                    IOUtils.getChars(year, 4, buf);
                } else {
                    buf = "0000-00-00T00:00:00".toCharArray();
                    IOUtils.getChars(second, 19, buf);
                    IOUtils.getChars(minute, 16, buf);
                    IOUtils.getChars(hour, 13, buf);
                    IOUtils.getChars(day, 10, buf);
                    IOUtils.getChars(month, 7, buf);
                    IOUtils.getChars(year, 4, buf);
                }
            }

            out.write(buf);

            int timeZone = calendar.getTimeZone().getRawOffset() / (3600 * 1000);
            if (timeZone == 0) {
                out.append("Z");
            } else if (timeZone > 0) {
                out.append("+").append(String.format("%02d", timeZone)).append(":00");
            } else {
                out.append("-").append(String.format("%02d", -timeZone)).append(":00");
            }

            if (out.isEnabled(SerializerFeature.UseSingleQuotes)) {
                out.append('\'');
            } else {
                out.append('\"');
            }
        } else {
            Date date = calendar.getTime();
            serializer.write(date);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Object value = DateCodec.instance.deserialze(parser, type, fieldName);

        if (value instanceof Calendar) {
            return (T) value;
        }

        Date date = (Date) value;
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return (T) calendar;
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
