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
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.*;

public class Jdk8DateCodec extends ContextObjectDeserializer implements ObjectSerializer, ContextObjectSerializer, ObjectDeserializer {

    public static final Jdk8DateCodec      instance            = new Jdk8DateCodec();

    private final static String            defaultPatttern     = "yyyy-MM-dd HH:mm:ss";
    private final static DateTimeFormatter defaultFormatter    = DateTimeFormatter.ofPattern(defaultPatttern);
    private final static DateTimeFormatter defaultFormatter_23 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private final static DateTimeFormatter formatter_dt19_tw   = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_cn   = DateTimeFormatter.ofPattern("yyyy年M月d日 HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_cn_1 = DateTimeFormatter.ofPattern("yyyy年M月d日 H时m分s秒");
    private final static DateTimeFormatter formatter_dt19_kr   = DateTimeFormatter.ofPattern("yyyy년M월d일 HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_us   = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_eur  = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_de   = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private final static DateTimeFormatter formatter_dt19_in   = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final static DateTimeFormatter formatter_d8        = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final static DateTimeFormatter formatter_d10_tw    = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private final static DateTimeFormatter formatter_d10_cn    = DateTimeFormatter.ofPattern("yyyy年M月d日");
    private final static DateTimeFormatter formatter_d10_kr    = DateTimeFormatter.ofPattern("yyyy년M월d일");
    private final static DateTimeFormatter formatter_d10_us    = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final static DateTimeFormatter formatter_d10_eur   = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final static DateTimeFormatter formatter_d10_de    = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final static DateTimeFormatter formatter_d10_in    = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final static DateTimeFormatter ISO_FIXED_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    private final static String formatter_iso8601_pattern     = "yyyy-MM-dd'T'HH:mm:ss";
    private final static String formatter_iso8601_pattern_23     = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private final static String formatter_iso8601_pattern_29     = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS";
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
                LocalTime localTime;
                if (text.length() == 23) {
                    LocalDateTime localDateTime = LocalDateTime.parse(text);
                    localTime = LocalTime.of(localDateTime.getHour(), localDateTime.getMinute(),
                            localDateTime.getSecond(), localDateTime.getNano());
                } else {
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
                        localTime = LocalDateTime
                                .ofInstant(
                                        Instant.ofEpochMilli(epochMillis),
                                        JSON.defaultTimeZone.toZoneId())
                                .toLocalTime();
                    } else {
                        localTime = LocalTime.parse(text);
                    }
                }
                return (T) localTime;
            } else if (type == ZonedDateTime.class) {
                if (formatter == defaultFormatter) {
                    formatter = ISO_FIXED_FORMAT;
                }

                if (formatter == null) {
                    if (text.length() <= 19) {
                        JSONScanner s = new JSONScanner(text);
                        TimeZone timeZone = parser.lexer.getTimeZone();
                        s.setTimeZone(timeZone);
                        boolean match = s.scanISO8601DateIfMatch(false);
                        if (match) {
                            Date date = s.getCalendar().getTime();
                            return (T) ZonedDateTime.ofInstant(date.toInstant(), timeZone.toZoneId());
                        }
                    }

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
                    return (T) Instant.ofEpochMilli(epochMillis);
                }

                Instant instant = Instant.parse(text);

                return (T) instant;
            }
        } else if (lexer.token() == JSONToken.LITERAL_INT) {
            long millis = lexer.longValue();
            lexer.nextToken();

            if ("unixtime".equals(format)) {
                millis *= 1000;
            } else if ("yyyyMMddHHmmss".equals(format)) {
                int yyyy = (int) (millis / 10000000000L);
                int MM = (int) ((millis / 100000000L) % 100);
                int dd = (int) ((millis / 1000000L) % 100);
                int HH = (int) ((millis / 10000L) % 100);
                int mm = (int) ((millis / 100L) % 100);
                int ss = (int) (millis % 100);

                if (type == LocalDateTime.class) {
                    return (T) LocalDateTime.of(yyyy, MM, dd, HH, mm, ss);
                }
            }

            if (type == LocalDateTime.class) {
                return (T) LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), JSON.defaultTimeZone.toZoneId());
            }

            if (type == LocalDate.class) {
                return (T) LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), JSON.defaultTimeZone.toZoneId()).toLocalDate();
            }
            if (type == LocalTime.class) {
                return (T) LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), JSON.defaultTimeZone.toZoneId()).toLocalTime();
            }

            if (type == ZonedDateTime.class) {
                return (T) ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), JSON.defaultTimeZone.toZoneId());
            }

            if (type == Instant.class) {
                return (T) Instant.ofEpochMilli(millis);
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
                    if (c4 == '-' && c7 == '-') {
                        if (c10 == 'T') {
                            formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
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
        }

        if (formatter == null) {
            JSONScanner dateScanner = new JSONScanner(text);
            if (dateScanner.scanISO8601DateIfMatch(false)) {
                Instant instant = dateScanner.getCalendar().toInstant();
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
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
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), JSON.defaultTimeZone.toZoneId());
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
                return LocalDateTime
                        .ofInstant(
                                Instant.ofEpochMilli(epochMillis),
                                JSON.defaultTimeZone.toZoneId())
                        .toLocalDate();
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
                return ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), JSON.defaultTimeZone.toZoneId());
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

                if (format == null) {
                    if ((features & mask) != 0 || serializer.isEnabled(SerializerFeature.UseISO8601DateFormat)) {
                        format = formatter_iso8601_pattern;
                    } else {
                        int nano = dateTime.getNano();
                        if (nano == 0) {
                            format = formatter_iso8601_pattern;
                        } else if (nano % 1000000 == 0) {
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
                    out.writeLong(dateTime.atZone(JSON.defaultTimeZone.toZoneId()).toInstant().toEpochMilli());
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
        if ("unixtime".equals(format)) {
            Instant instant = null;
            if (object instanceof ChronoZonedDateTime) {
                long seconds = ((ChronoZonedDateTime) object).toEpochSecond();
                out.writeInt((int) seconds);
                return;
            }

            if (object instanceof LocalDateTime) {
                long seconds = ((LocalDateTime) object).atZone(JSON.defaultTimeZone.toZoneId()).toEpochSecond();
                out.writeInt((int) seconds);
                return;
            }
        }

        if ("millis".equals(format)) {
            Instant instant = null;
            if (object instanceof ChronoZonedDateTime) {
                instant = ((ChronoZonedDateTime) object).toInstant();
            } else if (object instanceof LocalDateTime) {
                instant = ((LocalDateTime) object).atZone(JSON.defaultTimeZone.toZoneId()).toInstant();
            }
            if (instant != null) {
                long millis = instant.toEpochMilli();
                out.writeLong(millis);
                return;
            }
        }

        if (format == formatter_iso8601_pattern) {
            formatter = formatter_iso8601;
        } else {
            formatter = DateTimeFormatter.ofPattern(format);
        }

        String text = formatter.format((TemporalAccessor) object);
        out.writeString(text);
    }
}
