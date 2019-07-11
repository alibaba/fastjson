package com.alibaba.fastjson.serializer.formatter;

import com.alibaba.fastjson.JSON;
import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by jiangyu on 2019-03-08 23:47.
 */
public class JodaFormatter implements TimeFormatter {

  public final static JodaFormatter FORMATTER = new JodaFormatter();

  private final static String formatter_iso8601_pattern = "yyyy-MM-dd'T'HH:mm:ss";
  private final static DateTimeFormatter formatter_iso8601 = DateTimeFormat.forPattern(formatter_iso8601_pattern)
      .withZone(DateTimeZone.forID(JSON.defaultTimeZone.getID())).withLocale(JSON.defaultLocale);


  private JodaFormatter() {
  }

  @Override
  public Object format(String format, Object time) {
    DateTimeFormatter dateFormat;
    if (format == null || format.equals("")) {
      format = formatter_iso8601_pattern;
    }
    if (supportUnixTime(format, time)) {
      return toUnixTime(time);
    }
    if (formatter_iso8601_pattern.equals(format) && JSON.defaultTimeZone.equals(TimeZone.getDefault())
        && JSON.defaultLocale.equals(Locale.getDefault())) {
      dateFormat = formatter_iso8601;
    } else {
      dateFormat = DateTimeFormat.forPattern(format).withZone(DateTimeZone.forID(JSON.defaultTimeZone.getID()))
          .withLocale(JSON.defaultLocale);
    }
    return dateFormat.print((ReadablePartial) time);
  }

  @Override
  public long toUnixTime(Object time) {
    return 0;
  }

  @Override
  public boolean supportUnixTime(String format, Object time) {
    return false;
  }

  @Override
  public boolean accept(Class type) {
    return type != null && ReadablePartial.class.isAssignableFrom(type);
  }
}
