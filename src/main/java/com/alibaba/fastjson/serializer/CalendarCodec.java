package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.IOUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class CalendarCodec implements ObjectSerializer, ObjectDeserializer {

    public final static CalendarCodec instance = new CalendarCodec();

    private DatatypeFactory dateFactory;

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
                                                                                                               throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        Calendar calendar;
        if (object instanceof XMLGregorianCalendar) {
            calendar = ((XMLGregorianCalendar) object).toGregorianCalendar();
        } else {
            calendar = (Calendar) object;
        }

        if (out.isEnabled(SerializerFeature.UseISO8601DateFormat)) {
            final char quote = out.isEnabled(SerializerFeature.UseSingleQuotes) //
                ? '\'' //
                : '\"';
            out.append(quote);

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

            int timeZone = calendar.getTimeZone().getOffset(calendar.getTimeInMillis()) / (3600 * 1000);
            if (timeZone == 0) {
                out.append("Z");
            } else if (timeZone > 0) {
                out.append("+").append(String.format("%02d", timeZone)).append(":00");
            } else {
                out.append("-").append(String.format("%02d", -timeZone)).append(":00");
            }

            out.append(quote);
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

        JSONLexer lexer = parser.lexer;
        Calendar calendar = Calendar.getInstance(lexer.getTimeZone(), lexer.getLocale());
        calendar.setTime(date);

        if (type == XMLGregorianCalendar.class) {
            return (T) createXMLGregorianCalendar((GregorianCalendar) calendar);
        }

        return (T) calendar;
    }

    public XMLGregorianCalendar createXMLGregorianCalendar(Calendar calendar) {
        if (dateFactory == null) {
            try {
                dateFactory = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException e) {
                throw new IllegalStateException("Could not obtain an instance of DatatypeFactory.", e);
            }
        }
        return dateFactory.newXMLGregorianCalendar((GregorianCalendar) calendar);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
