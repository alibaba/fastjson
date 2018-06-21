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
import java.util.Locale;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.*;

public class Jdk8DateCodec extends ContextObjectDeserializer implements ObjectSerializer, ContextObjectSerializer, ObjectDeserializer {

    public static final Jdk8DateCodec      instance           = new Jdk8DateCodec();

    private final static String            defaultPatttern    = "yyyy-MM-dd HH:mm:ss";
    private final static DateTimeFormatter defaultFormatter   = DateTimeFormatter.ofPattern(defaultPatttern);
    private final static DateTimeFormatter formatter_dt19_tw  = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_cn  = DateTimeFormatter.ofPattern("yyyy年M月d日 HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_cn_1  = DateTimeFormatter.ofPattern("yyyy年M月d日 H时m分s秒");
    private final static DateTimeFormatter formatter_dt19_kr  = DateTimeFormatter.ofPattern("yyyy년M월d일 HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_us  = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_eur = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_de  = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_in  = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final static DateTimeFormatter formatter_d8       = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final static DateTimeFormatter formatter_d10_tw   = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private final static DateTimeFormatter formatter_d10_cn   = DateTimeFormatter.ofPattern("yyyy年M月d日");
    private final static DateTimeFormatter formatter_d10_kr   = DateTimeFormatter.ofPattern("yyyy년M월d일");
    private final static DateTimeFormatter formatter_d10_us   = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final static DateTimeFormatter formatter_d10_eur  = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final static DateTimeFormatter formatter_d10_de   = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final static DateTimeFormatter formatter_d10_in   = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final static DateTimeFormatter ISO_FIXED_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    private final static String formatter_iso8601_pattern     = "yyyy-MM-dd'T'HH:mm:ss";
    private final static DateTimeFormatter formatter_iso8601  = DateTimeFormatter.ofPattern(formatter_iso8601_pattern);

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName, String format, int feature) {
        JSONLexer lexer = parser.lexer;
        if (lexer.token() == JSONToken.NULL){
            lexer.nextToken();
            return null;
        }

        if (lexer.token() == JSONToken.LITERAL_STRING) {
            String text = lexer.stringVal();
            lexer.nextToken();

            DateTimeFormatter formatter = null;
            if (format != null) {
                if (defaultPatttern.equals(format)) {
                    formatter = defaultFormatter;
                } else {
                    formatter = DateTimeFormatter.ofPattern(format);
                }
            }
            
            if ("".equals(text)) {
                return null;
            }

            if (type == LocalDateTime.class) {
                LocalDateTime localDateTime;
                if (text.length() == 10 || text.length() == 8) {
                    LocalDate localDate = parseLocalDate(text, format, formatter);
                    localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
                } else {
                    localDateTime = parseDateTime(text, formatter);
                }
                return (T) localDateTime;
            } else if (type == LocalDate.class) {
                LocalDate localDate;
                if (text.length() == 23) {
                    LocalDateTime localDateTime = LocalDateTime.parse(text);
                    localDate = LocalDate.of(localDateTime.getYear(), localDateTime.getMonthValue(),
                                             localDateTime.getDayOfMonth());
                } else {
                    localDate = parseLocalDate(text, format, formatter);
                }

                return (T) localDate;
            } else if (type == LocalTime.class) {
                LocalTime localDate;
                if (text.length() == 23) {
                    LocalDateTime localDateTime = LocalDateTime.parse(text);
                    localDate = LocalTime.of(localDateTime.getHour(), localDateTime.getMinute(),
                                             localDateTime.getSecond(), localDateTime.getNano());
                } else {
                    localDate = LocalTime.parse(text);
                }
                return (T) localDate;
            } else if (type == ZonedDateTime.class) {
                if (formatter == defaultFormatter) {
                    formatter = ISO_FIXED_FORMAT;
                }

                ZonedDateTime zonedDateTime = parseZonedDateTime(text, formatter);

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

    protected LocalDateTime parseDateTime(String text, DateTimeFormatter formatter) {
        if (formatter == null) {
            if (text.length() == 19) {
                char c4 = text.charAt(4);
                char c7 = text.charAt(7);
                char c10 = text.charAt(10);
                char c13 = text.charAt(13);
                char c16 = text.charAt(16);
                if (c13 == ':' && c16 == ':') {
                    if (c4 == '-' && c7 == '-') {
                        if (c10 == 'T') {
                            formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                        } else if (c10 == ' ') {
                            formatter = defaultFormatter;
                        }
                    } else if (c4 == '-' && c7 == '-') {
                        formatter = defaultFormatter;
                    } else if (c4 == '/' && c7 == '/') { // tw yyyy/mm/dd
                        formatter = formatter_dt19_tw;
                    } else {
                        char c0 = text.charAt(0);
                        char c1 = text.charAt(1);
                        char c2 = text.charAt(2);
                        char c3 = text.charAt(3);
                        char c5 = text.charAt(5);
                        if (c2 == '/' && c5 == '/') { // mm/dd/yyyy or mm/dd/yyyy
                            int v0 = (c0 - '0') * 10 + (c1 - '0');
                            int v1 = (c3 - '0') * 10 + (c4 - '0');
                            if (v0 > 12) {
                                formatter = formatter_dt19_eur;
                            } else if (v1 > 12) {
                                formatter = formatter_dt19_us;
                            } else {
                                String country = Locale.getDefault().getCountry();

                                if (country.equals("US")) {
                                    formatter = formatter_dt19_us;
                                } else if (country.equals("BR") //
                                           || country.equals("AU")) {
                                    formatter = formatter_dt19_eur;
                                }
                            }
                        } else if (c2 == '.' && c5 == '.') { // dd.mm.yyyy
                            formatter = formatter_dt19_de;
                        } else if (c2 == '-' && c5 == '-') { // dd-mm-yyyy
                            formatter = formatter_dt19_in;
                        }
                    }
                }
            }

            if (text.length() >= 17) {
                char c4 = text.charAt(4);
                if (c4 == '年') {
                    if (text.charAt(text.length() - 1) == '秒') {
                        formatter = formatter_dt19_cn_1;    
                    } else {
                        formatter = formatter_dt19_cn;
                    }
                } else if (c4 == '년') {
                    formatter = formatter_dt19_kr;
                }
            }
        }

        return formatter == null ? //
            LocalDateTime.parse(text) //
            : LocalDateTime.parse(text, formatter);
    }

    protected LocalDate parseLocalDate(String text, String format, DateTimeFormatter formatter) {
        if (formatter == null) {
            if (text.length() == 8) {
                formatter = formatter_d8;
            }

            if (text.length() == 10) {
                char c4 = text.charAt(4);
                char c7 = text.charAt(7);
                if (c4 == '/' && c7 == '/') { // tw yyyy/mm/dd
                    formatter = formatter_d10_tw;
                }

                char c0 = text.charAt(0);
                char c1 = text.charAt(1);
                char c2 = text.charAt(2);
                char c3 = text.charAt(3);
                char c5 = text.charAt(5);
                if (c2 == '/' && c5 == '/') { // mm/dd/yyyy or mm/dd/yyyy
                    int v0 = (c0 - '0') * 10 + (c1 - '0');
                    int v1 = (c3 - '0') * 10 + (c4 - '0');
                    if (v0 > 12) {
                        formatter = formatter_d10_eur;
                    } else if (v1 > 12) {
                        formatter = formatter_d10_us;
                    } else {
                        String country = Locale.getDefault().getCountry();

                        if (country.equals("US")) {
                            formatter = formatter_d10_us;
                        } else if (country.equals("BR") //
                                   || country.equals("AU")) {
                            formatter = formatter_d10_eur;
                        }
                    }
                } else if (c2 == '.' && c5 == '.') { // dd.mm.yyyy
                    formatter = formatter_d10_de;
                } else if (c2 == '-' && c5 == '-') { // dd-mm-yyyy
                    formatter = formatter_d10_in;
                }
            }

            if (text.length() >= 9) {
                char c4 = text.charAt(4);
                if (c4 == '年') {
                    formatter = formatter_d10_cn;
                } else if (c4 == '년') {
                    formatter = formatter_d10_kr;
                }
            }
        }

        return formatter == null ? //
            LocalDate.parse(text) //
            : LocalDate.parse(text, formatter);
    }

    protected ZonedDateTime parseZonedDateTime(String text, DateTimeFormatter formatter) {
        if (formatter == null) {
            if (text.length() == 19) {
                char c4 = text.charAt(4);
                char c7 = text.charAt(7);
                char c10 = text.charAt(10);
                char c13 = text.charAt(13);
                char c16 = text.charAt(16);
                if (c13 == ':' && c16 == ':') {
                    if (c4 == '-' && c7 == '-') {
                        if (c10 == 'T') {
                            formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                        } else if (c10 == ' ') {
                            formatter = defaultFormatter;
                        }
                    } else if (c4 == '-' && c7 == '-') {
                        formatter = defaultFormatter;
                    } else if (c4 == '/' && c7 == '/') { // tw yyyy/mm/dd
                        formatter = formatter_dt19_tw;
                    } else {
                        char c0 = text.charAt(0);
                        char c1 = text.charAt(1);
                        char c2 = text.charAt(2);
                        char c3 = text.charAt(3);
                        char c5 = text.charAt(5);
                        if (c2 == '/' && c5 == '/') { // mm/dd/yyyy or mm/dd/yyyy
                            int v0 = (c0 - '0') * 10 + (c1 - '0');
                            int v1 = (c3 - '0') * 10 + (c4 - '0');
                            if (v0 > 12) {
                                formatter = formatter_dt19_eur;
                            } else if (v1 > 12) {
                                formatter = formatter_dt19_us;
                            } else {
                                String country = Locale.getDefault().getCountry();

                                if (country.equals("US")) {
                                    formatter = formatter_dt19_us;
                                } else if (country.equals("BR") //
                                        || country.equals("AU")) {
                                    formatter = formatter_dt19_eur;
                                }
                            }
                        } else if (c2 == '.' && c5 == '.') { // dd.mm.yyyy
                            formatter = formatter_dt19_de;
                        } else if (c2 == '-' && c5 == '-') { // dd-mm-yyyy
                            formatter = formatter_dt19_in;
                        }
                    }
                }
            }

            if (text.length() >= 17) {
                char c4 = text.charAt(4);
                if (c4 == '年') {
                    if (text.charAt(text.length() - 1) == '秒') {
                        formatter = formatter_dt19_cn_1;
                    } else {
                        formatter = formatter_dt19_cn;
                    }
                } else if (c4 == '년') {
                    formatter = formatter_dt19_kr;
                }
            }
        }

        return formatter == null ? //
                ZonedDateTime.parse(text) //
                : ZonedDateTime.parse(text, formatter);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
        } else {
            if (fieldType == null) {
                fieldType = object.getClass();
            }

            if (fieldType == LocalDateTime.class) {
                final int mask = SerializerFeature.UseISO8601DateFormat.getMask();
                LocalDateTime dateTime = (LocalDateTime) object;
                String format = serializer.getDateFormatPattern();

                if (format == null && (features & mask) != 0 || serializer.isEnabled(SerializerFeature.UseISO8601DateFormat)) {
                    format = formatter_iso8601_pattern;
                }

                if (dateTime.getNano() == 0 || format != null) {

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

    public void write(JSONSerializer serializer, Object object, BeanContext context) throws IOException {
        SerializeWriter out = serializer.out;
        String format = context.getFormat();
        write(out, (TemporalAccessor) object, format);
    }

    private void write(SerializeWriter out, TemporalAccessor object, String format) {
        DateTimeFormatter formatter;
        if (format == formatter_iso8601_pattern) {
            formatter = formatter_iso8601;
        } else {
            formatter = DateTimeFormatter.ofPattern(format);
        }

        String text = formatter.format((TemporalAccessor) object);
        out.writeString(text);
    }
}
