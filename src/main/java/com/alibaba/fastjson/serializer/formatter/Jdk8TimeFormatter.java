package com.alibaba.fastjson.serializer.formatter;

import com.alibaba.fastjson.JSON;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Created by jiangyu on 2019-03-08 23:47.
 */
public class Jdk8TimeFormatter implements TimeFormatter {

  public final static Jdk8TimeFormatter FORMATTER = new Jdk8TimeFormatter();

  private final static String formatter_iso8601_pattern = "yyyy-MM-dd HH:mm:ss";
  private final static DateTimeFormatter formatter_iso8601 = build(formatter_iso8601_pattern, JSON.defaultTimeZone,
      JSON.defaultLocale);

  private static DateTimeFormatter build(String format, TimeZone zone, Locale locale) {
    return DateTimeFormatter.ofPattern(format).withZone(zone.toZoneId()).withLocale(locale);
  }

  private Jdk8TimeFormatter() {
  }

  @Override
  public Object format(String format, Object time) {
    try {
      DateTimeFormatter formatter;
      if (format == null || format.equals("")) {
        format = formatter_iso8601_pattern;
      }
      if (supportUnixTime(format, time)) {
        return toUnixTime(time);
      }
      if (Objects.equals(formatter_iso8601_pattern, format) && Objects
          .equals(JSON.defaultTimeZone, TimeZone.getDefault()) && Objects
          .equals(JSON.defaultLocale, Locale.getDefault())) {
        formatter = formatter_iso8601;
      } else {
        formatter = build(format, JSON.defaultTimeZone, JSON.defaultLocale);
      }
      return formatter.format((TemporalAccessor) time);
    } catch (Exception e) {
      System.out.println(format + " " + time);
      return time.toString();
    }
  }

  @Override
  public long toUnixTime(Object time) {
    return ChronoZonedDateTime.class.cast(time).toInstant().toEpochMilli();
  }

  @Override
  public boolean supportUnixTime(String format, Object time) {
    return Objects.equals(UNIXTIME_FORMAT, format) && time instanceof ChronoZonedDateTime;
  }

  @Override
  public boolean accept(Class type) {
    return type != null && TemporalAccessor.class.isAssignableFrom(type);
  }
}
