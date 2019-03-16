package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ContextObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.IOUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class CalendarCodec extends ContextObjectDeserializer implements ObjectSerializer, ObjectDeserializer, ContextObjectSerializer {

    public final static CalendarCodec instance = new CalendarCodec();

    private DatatypeFactory dateFactory;

    public void write(JSONSerializer serializer, Object object, BeanContext context) throws IOException {
        SerializeWriter out = serializer.out;
        String format = context.getFormat();
        Calendar calendar = (Calendar) object;

        if (format.equals("unixtime")) {
            long seconds = calendar.getTimeInMillis() / 1000L;
            out.writeInt((int) seconds);
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat(format);
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(JSON.DEFFAULT_DATE_FORMAT, serializer.locale);
        }
        dateFormat.setTimeZone(serializer.timeZone);
        String text = dateFormat.format(calendar.getTime());
        out.writeString(text);
    }


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

            float timeZoneF = calendar.getTimeZone().getOffset(calendar.getTimeInMillis()) / (3600.0f * 1000);
            int timeZone = (int)timeZoneF;
            if (timeZone == 0.0) {
                out.write('Z');
            } else {
                if (timeZone > 9) {
                    out.write('+');
                    out.writeInt(timeZone);
                } else if (timeZone > 0) {
                    out.write('+');
                    out.write('0');
                    out.writeInt(timeZone);
                } else if (timeZone < -9) {
                    out.write('-');
                    out.writeInt(timeZone);
                } else if (timeZone < 0) {
                    out.write('-');
                    out.write('0');
                    out.writeInt(-timeZone);
                }
                out.write(':');
                // handles uneven timeZones 30 mins, 45 mins
                // this would always be less than 60
                int offSet = (int)((timeZoneF - timeZone) * 60);
                out.append(String.format("%02d", offSet));
            }

            out.append(quote);
        } else {
            Date date = calendar.getTime();
            serializer.write(date);
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        return deserialze(parser, clazz, fieldName, null, 0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName, String format, int features) {
        Object value = DateCodec.instance.deserialze(parser, type, fieldName, format, features);

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
