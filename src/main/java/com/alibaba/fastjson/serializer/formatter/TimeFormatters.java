package com.alibaba.fastjson.serializer.formatter;

import com.alibaba.fastjson.util.ClassUtils;
import com.alibaba.fastjson.util.JavaVersion;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangyu on 2019-03-08 23:45.
 */
public class TimeFormatters implements TimeFormatter {

  public final static TimeFormatter TIME_FORMATTER = new TimeFormatters();

  private List<TimeFormatter> formatterList;
  private TimeFormatter EMPTY = new EmptyFormatter();

  private TimeFormatters() {
    this.formatterList = new ArrayList<TimeFormatter>(4);
    formatterList.add(DateFormatter.FORMATTER);
    if (JavaVersion.isJava8OrUpper()) {
      TimeFormatter java8Format = (TimeFormatter) ClassUtils
          .getFieldValue("com.alibaba.fastjson.serializer.formatter.Jdk8TimeFormatter", "FORMATTER", null);
      if (java8Format != null) {
        formatterList.add(java8Format);
      }
    }
    if (ClassUtils.exist("org.joda.time.ReadablePartial")) {
      TimeFormatter jodaFormat = (TimeFormatter) ClassUtils
          .getFieldValue("com.alibaba.fastjson.serializer.formatter.JodaFormatter", "FORMATTER", null);
      if (jodaFormat != null) {
        formatterList.add(jodaFormat);
      }
      formatterList.add(JodaFormatter.FORMATTER);
    }
    formatterList.add(EMPTY);
  }

  private TimeFormatter getFormatter(String format, Object timeObject) {
    if (format == null || timeObject == null) {
      return EMPTY;
    }
    for (TimeFormatter formatter : formatterList) {
      if (formatter.accept(timeObject.getClass())) {
        return formatter;
      }
    }
    return EMPTY;
  }

  @Override
  public Object format(String format, Object time) {
    TimeFormatter formatter = getFormatter(format, time);
    return formatter.format(format, time);
  }

  @Override
  public long toUnixTime(Object time) {
    for (TimeFormatter formatter : formatterList) {
      if (formatter.supportUnixTime(UNIXTIME_FORMAT, time)) {
        return toUnixTime(time);
      }
    }
    return 0;
  }

  @Override
  public boolean supportUnixTime(String format, Object time) {
    for (TimeFormatter formatter : formatterList) {
      if (formatter.supportUnixTime(format, time)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean accept(Class type) {
    return true;
  }
}
