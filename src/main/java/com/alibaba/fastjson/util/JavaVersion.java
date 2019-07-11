package com.alibaba.fastjson.util;

/**
 * Created by jiangyu on 2019-03-09 09:24.
 */
public class JavaVersion {

  private static final String javaVersion;
  private static int javaVersionVal = 6;

  static {
    javaVersion = System.getProperty("java.version");
    if (javaVersion != null) {
      String[] tokens = javaVersion.split("\\.");
      if (tokens.length >= 1) {
        try {
          javaVersionVal = Integer.valueOf(tokens[1]);
        } catch (Exception e) {
        }
      }
    }
  }

  public static String getJavaVersion() {
    return javaVersion;
  }

  public static boolean isJava8OrUpper() {
    return javaVersionVal >= 8;
  }
}
