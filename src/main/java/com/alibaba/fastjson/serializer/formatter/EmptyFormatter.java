package com.alibaba.fastjson.serializer.formatter;

/**
 * Created by jiangyu on 2019-03-08 23:47.
 */
public class EmptyFormatter implements TimeFormatter {

  @Override
  public Object format(String format, Object time) {
    return time;
  }

  @Override
  public long toUnixTime(Object time) {
    if (Long.TYPE == time.getClass() || Long.class == time.getClass()) {
      return (Long) time;
    }
    return 0;
  }

  @Override
  public boolean supportUnixTime(String format, Object time) {
    return Long.TYPE == time.getClass() || Long.class == time.getClass();
  }

  @Override
  public boolean accept(Class type) {
    return true;
  }
}
