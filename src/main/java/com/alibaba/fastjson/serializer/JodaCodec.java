package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.joda.time.*;
import org.joda.time.format.*;

public class JodaCodec implements ObjectSerializer, ContextObjectSerializer, ObjectDeserializer {
    public final static JodaCodec instance = new JodaCodec();

    private final static String            defaultPatttern     = "yyyy-MM-dd HH:mm:ss";
    private final static DateTimeFormatter defaultFormatter    = DateTimeFormat.forPattern(defaultPatttern);
    private final static DateTimeFormatter defaultFormatter_23 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private final static DateTimeFormatter formatter_dt19_tw   = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_cn   = DateTimeFormat.forPattern("yyyy年M月d日 HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_cn_1 = DateTimeFormat.forPattern("yyyy年M月d日 H时m分s秒");
    private final static DateTimeFormatter formatter_dt19_kr   = DateTimeFormat.forPattern("yyyy년M월d일 HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_us   = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_eur  = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_de   = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_in   = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");

    private final static DateTimeFormatter formatter_d8        = DateTimeFormat.forPattern("yyyyMMdd");
    private final static DateTimeFormatter formatter_d10_tw    = DateTimeFormat.forPattern("yyyy/MM/dd");
    private final static DateTimeFormatter formatter_d10_cn    = DateTimeFormat.forPattern("yyyy年M月d日");
    private final static DateTimeFormatter formatter_d10_kr    = DateTimeFormat.forPattern("yyyy년M월d일");
    private final static DateTimeFormatter formatter_d10_us    = DateTimeFormat.forPattern("MM/dd/yyyy");
    private final static DateTimeFormatter formatter_d10_eur   = DateTimeFormat.forPattern("dd/MM/yyyy");
    private final static DateTimeFormatter formatter_d10_de    = DateTimeFormat.forPattern("dd.MM.yyyy");
    private final static DateTimeFormatter formatter_d10_in    = DateTimeFormat.forPattern("dd-MM-yyyy");

    private final static DateTimeFormatter ISO_FIXED_FORMAT =
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZone(DateTimeZone.getDefault());

    private final static String formatter_iso8601_pattern     = "yyyy-MM-dd'T'HH:mm:ss";
    private final static String formatter_iso8601_pattern_23     = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private final static String formatter_iso8601_pattern_29     = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS";
    private final static DateTimeFormatter formatter_iso8601  = DateTimeFormat.forPattern(formatter_iso8601_pattern);


    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        return deserialze(parser, type, fieldName, null, 0);
    }

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
                    formatter = DateTimeFormat.forPattern(format);
                }
            }

            if ("".equals(text)) {
                return null;
            }

            if (type == LocalDateTime.class) {
                LocalDateTime localDateTime;
                if (text.length() == 10 || text.length() == 8) {
                    LocalDate localDate = parseLocalDate(text, format, formatter);
                    localDateTime = localDate.toLocalDateTime(LocalTime.MIDNIGHT);
                } else {
                    localDateTime = parseDateTime(text, formatter);
                }
                return (T) localDateTime;
            } else if (type == LocalDate.class) {
                LocalDate localDate;
                if (text.length() == 23) {
                    LocalDateTime localDateTime = LocalDateTime.parse(text);
                    localDate = localDateTime.toLocalDate();
                } else {
                    localDate = parseLocalDate(text, format, formatter);
                }

                return (T) localDate;
            } else if (type == LocalTime.class) {
                LocalTime localDate;
                if (text.length() == 23) {
                    LocalDateTime localDateTime = LocalDateTime.parse(text);
                    localDate = localDateTime.toLocalTime();
                } else {
                    localDate = LocalTime.parse(text);
                }
                return (T) localDate;
            } else if (type == DateTime.class) {
                if (formatter == defaultFormatter) {
                    formatter = ISO_FIXED_FORMAT;
                }

                DateTime zonedDateTime = parseZonedDateTime(text, formatter);

                return (T) zonedDateTime;
            } else if (type == DateTimeZone.class) {
                DateTimeZone offsetTime = DateTimeZone.forID(text);

                return (T) offsetTime;
            } else if (type == Period.class) {
                Period period = Period.parse(text);

                return (T) period;
            } else if (type == Duration.class) {
                Duration duration = Duration.parse(text);

                return (T) duration;
            } else if (type == Instant.class) {
                boolean digit = true;
                for (int i = 0; i < text.length(); ++i) {
                    char ch = text.charAt(i);
                    if (ch < '0' || ch > '9') {
                        digit = false;
                        break;
                    }
                }
                if (digit && text.length() > 8 && text.length() < 19) {
                    long epochMillis = Long.parseLong(text);
                    return (T) new Instant(epochMillis);
                }

                Instant instant = Instant.parse(text);

                return (T) instant;
            } else if (type == DateTimeFormatter.class) {
                return (T) DateTimeFormat.forPattern(text);
            }
        } else if (lexer.token() == JSONToken.LITERAL_INT) {
            long millis = lexer.longValue();
            lexer.nextToken();

            TimeZone timeZone = JSON.defaultTimeZone;
            if (timeZone == null) {
                timeZone = TimeZone.getDefault();
            }

            if (type == DateTime.class) {
                return (T) new DateTime(millis, DateTimeZone.forTimeZone(timeZone));
            }

            LocalDateTime localDateTime =  new LocalDateTime(millis, DateTimeZone.forTimeZone(timeZone));
            if (type == LocalDateTime.class) {
                return (T) localDateTime;
            }

            if (type == LocalDate.class) {
                return (T) localDateTime.toLocalDate();
            }

            if (type == LocalTime.class) {
                return (T) localDateTime.toLocalTime();
            }

            if (type == Instant.class) {
                Instant instant = new Instant(millis);

                return (T) instant;
            }

            throw new UnsupportedOperationException();
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
                    if (c4 == '-' && c7 == '-') { // yyyy-MM-dd  or  yyyy-MM-dd'T'
                        if (c10 == 'T') {
                            formatter = formatter_iso8601;
                        } else if (c10 == ' ') {
                            formatter = defaultFormatter;
                        }
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
            } else if (text.length() == 23) {
                char c4 = text.charAt(4);
                char c7 = text.charAt(7);
                char c10 = text.charAt(10);
                char c13 = text.charAt(13);
                char c16 = text.charAt(16);
                char c19 = text.charAt(19);

                if (c13 == ':'
                        && c16 == ':'
                        && c4 == '-'
                        && c7 == '-'
                        && c10 == ' '
                        && c19 == '.'
                ) {
                    formatter = defaultFormatter_23;
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

            boolean digit = true;
            for (int i = 0; i < text.length(); ++i) {
                char ch = text.charAt(i);
                if (ch < '0' || ch > '9') {
                    digit = false;
                    break;
                }
            }
            if (digit && text.length() > 8 && text.length() < 19) {
                long epochMillis = Long.parseLong(text);
                return new LocalDateTime(epochMillis, DateTimeZone.forTimeZone(JSON.defaultTimeZone));
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

            boolean digit = true;
            for (int i = 0; i < text.length(); ++i) {
                char ch = text.charAt(i);
                if (ch < '0' || ch > '9') {
                    digit = false;
                    break;
                }
            }
            if (digit && text.length() > 8 && text.length() < 19) {
                long epochMillis = Long.parseLong(text);
                return new LocalDateTime(epochMillis, DateTimeZone.forTimeZone(JSON.defaultTimeZone))
                        .toLocalDate();
            }
        }

        return formatter == null ? //
                LocalDate.parse(text) //
                : LocalDate.parse(text, formatter);
    }

    protected DateTime parseZonedDateTime(String text, DateTimeFormatter formatter) {
        if (formatter == null) {
            if (text.length() == 19) {
                char c4 = text.charAt(4);
                char c7 = text.charAt(7);
                char c10 = text.charAt(10);
                char c13 = text.charAt(13);
                char c16 = text.charAt(16);
                if (c13 == ':' && c16 == ':') {
                    if (c4 == '-' && c7 == '-') { // yyyy-MM-dd  or  yyyy-MM-dd'T'
                        if (c10 == 'T') {
                            formatter = formatter_iso8601;
                        } else if (c10 == ' ') {
                            formatter = defaultFormatter;
                        }
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
                DateTime.parse(text) //
                : DateTime.parse(text, formatter);
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

                if (format == null) {
                    if ((features & mask) != 0 || serializer.isEnabled(SerializerFeature.UseISO8601DateFormat)) {
                        format = formatter_iso8601_pattern;
                    } else {
                        int millis = dateTime.getMillisOfSecond();
                        if (millis == 0) {
                            format = formatter_iso8601_pattern_23;
                        } else {
                            format = formatter_iso8601_pattern_29;
                        }
                    }
                }

                if (format != null) {
                    write(out, dateTime, format);
                } else if (out.isEnabled(SerializerFeature.WriteDateUseDateFormat)) {
                    //使用固定格式转化时间
                    write(out, dateTime, JSON.DEFFAULT_DATE_FORMAT);
                } else {
                    out.writeLong(dateTime.toDateTime(DateTimeZone.forTimeZone(JSON.defaultTimeZone)).toInstant().getMillis());
                }
            } else {
                out.writeString(object.toString());
            }
        }
    }

    public void write(JSONSerializer serializer, Object object, BeanContext context) throws IOException {
        SerializeWriter out = serializer.out;
        String format = context.getFormat();
        write(out, (ReadablePartial) object, format);
    }

    private void write(SerializeWriter out, ReadablePartial object, String format) {
        DateTimeFormatter formatter;
        if (format.equals(formatter_iso8601_pattern)) {
            formatter = formatter_iso8601;
        } else {
            formatter = DateTimeFormat.forPattern(format);
        }

        String text = formatter.print(object);
        out.writeString(text);
    }
}
