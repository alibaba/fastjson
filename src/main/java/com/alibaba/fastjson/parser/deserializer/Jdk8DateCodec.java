package com.alibaba.fastjson.parser.deserializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.ContextObjectSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class Jdk8DateCodec extends ContextObjectDeserializer implements ObjectSerializer, ContextObjectSerializer, ObjectDeserializer {

    public static final Jdk8DateCodec instance = new Jdk8DateCodec();
    
    private final static String defaultPatttern = "yyyy-MM-dd HH:mm:ss";
    private final static DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern(defaultPatttern);

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName, String format, int feature) {
        JSONLexer lexer = parser.lexer;
        if (lexer.token() == JSONToken.LITERAL_STRING) {
            String text = lexer.stringVal();
            lexer.nextToken();

            DateTimeFormatter formatter = null;
            if (format != null) {
                formatter = DateTimeFormatter.ofPattern(format);
            } else if (defaultPatttern.length() == text.length() // 
                    && parser.getDateFomartPattern().equals(defaultPatttern)) {
                formatter = defaultFormatter;
            }
            
            if (type == LocalDateTime.class) {
                LocalDateTime localDateTime;
                if (text.length() == 10 || text.length() == 8) {
                    LocalDate localDate = formatter == null ? // 
                        LocalDate.parse(text) // 
                        : LocalDate.parse(text, formatter);
                    localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
                } else {
                    localDateTime = formatter == null ? // 
                        LocalDateTime.parse(text) // 
                        : LocalDateTime.parse(text, formatter);
                }
                return (T) localDateTime;
            } else if (type == LocalDate.class) {
                LocalDate localDate;
                if (text.length() == 23) {
                    LocalDateTime localDateTime = LocalDateTime.parse(text);
                    localDate = LocalDate.of(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth());
                } else {
                    localDate = LocalDate.parse(text);
                }
                
                return (T) localDate;
            } else if (type == LocalTime.class) {
                LocalTime localDate;
                if (text.length() == 23) {
                    LocalDateTime localDateTime = LocalDateTime.parse(text);
                    localDate = LocalTime.of(localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond(), localDateTime.getNano());
                } else {
                    localDate = LocalTime.parse(text);
                }
                return (T) localDate;
            } else if (type == ZonedDateTime.class) {
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(text);

                return (T) zonedDateTime;
            } else if (type == OffsetDateTime.class) {
                OffsetDateTime offsetDateTime = OffsetDateTime.parse(text);

                return (T) offsetDateTime;
            } else if (type == OffsetTime.class) {
                OffsetTime offsetTime = OffsetTime.parse(text);
                
                return (T) offsetTime;
            } else if (type == ZoneId.class) {
                ZoneId offsetTime = ZoneId.of(text);
                
                return (T) offsetTime;
            } else if (type == Period.class) {
                Period period = Period.parse(text);
                
                return (T) period;
            } else if (type == Duration.class) {
                Duration duration = Duration.parse(text);
                
                return (T) duration;
            } else if (type == Instant.class) {
                Instant instant = Instant.parse(text);
                
                return (T) instant;
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return null;
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
        } else {
            if (fieldType == LocalDateTime.class) {
                    LocalDateTime dateTime = (LocalDateTime) object;
                    if (dateTime.getNano() == 0) {
                    String format = serializer.getDateFormatPattern();
                    if (format == null) {
                        format = JSON.DEFFAULT_DATE_FORMAT;
                    }
                    write(out, dateTime, format);
                } else {
                    out.writeString(object.toString());    
                }
            } else {
                out.writeString(object.toString());
            }
        }
    }

    @Override
    public void write(JSONSerializer serializer, Object object, BeanContext context) throws IOException {
        SerializeWriter out = serializer.out;
        String format = context.getFormat();
        write(out, (TemporalAccessor) object, format);
    }
    
    private void write(SerializeWriter out, TemporalAccessor object, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String text = formatter.format((TemporalAccessor) object);
        out.writeString(text);
    }
}
