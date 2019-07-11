package com.alibaba.fastjson.util;

import java.lang.reflect.Field;

/**
 * Created by jiangyu on 2019-03-09 09:37.
 */
public class ClassUtils {

  public static boolean exist(String clazz) {
    return load(clazz) != null;
  }

  public static Class load(String clazz) {
    try {
      return Class.forName(clazz);
    } catch (Exception e) {
      return null;
    }
  }

  public static Object getFieldValue(String className, String field, Object obj) {
    Class clazz = load(className);
    if (clazz != null) {
      try {
        Field f = clazz.getField(field);
        f.setAccessible(true);
        return f.get(obj);
      } catch (Exception e) {
        return null;
      }
    }
    return null;
  }

}
