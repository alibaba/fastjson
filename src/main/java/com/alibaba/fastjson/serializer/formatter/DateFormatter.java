package com.alibaba.fastjson.serializer.formatter;

import com.alibaba.fastjson.JSON;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jiangyu on 2019-03-08 23:47.
 */
public class DateFormatter implements TimeFormatter {

  public final static DateFormatter FORMATTER = new DateFormatter();

  private DateFormatter() {
  }

  @Override
  public Object format(String format, Object time) {
    if (format == null || format.equals("")) {
      format = JSON.DEFFAULT_DATE_FORMAT;
    }
    if (supportUnixTime(format, time)) {
      return toUnixTime(time);
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat(format, JSON.defaultLocale);
    dateFormat.setTimeZone(JSON.defaultTimeZone);
    if (time instanceof Calendar) {
      return dateFormat.format(Calendar.class.cast(time).getTime());
    } else {
      return dateFormat.format(time);
    }
  }

  @Override
  public long toUnixTime(Object time) {
    if (time instanceof Date) {
      return Date.class.cast(time).getTime();
    } else {
      return Calendar.class.cast(time).getTimeInMillis();
    }
  }

  @Override
  public boolean supportUnixTime(String format, Object time) {
    return UNIXTIME_FORMAT.equalsIgnoreCase(format);
  }

  @Override
  public boolean accept(Class type) {
    return type != null && (Date.class.isAssignableFrom(type) || Calendar.class.isAssignableFrom(type));
  }
}
