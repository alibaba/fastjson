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

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class Jdk8DateCodec implements ObjectSerializer, ObjectDeserializer {

    public static final Jdk8DateCodec instance = new Jdk8DateCodec();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.lexer;
        if (lexer.token() == JSONToken.LITERAL_STRING) {
            String text = lexer.stringVal();
            lexer.nextToken();

            if (type == LocalDateTime.class) {
                LocalDateTime localDateTime = LocalDateTime.parse(text);

                return (T) localDateTime;
            } else if (type == LocalDate.class) {
                LocalDate localDate = LocalDate.parse(text);

                return (T) localDate;
            } else if (type == LocalTime.class) {
                LocalTime localDate = LocalTime.parse(text);
                
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
            return;
        }
        
        out.writeString(object.toString());
    }

}
