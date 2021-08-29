package com.alibaba.fastjson.util;

import java.util.concurrent.Callable;

public class ModuleUtil {
    private static boolean hasJavaSql = false;

    static {
        try {
            Class.forName("java.sql.Time");
            hasJavaSql = true;
        } catch (Throwable e) {
            hasJavaSql = false;
        }
    }

    public static <T> T callWhenHasJavaSql(Callable<T> callable) {
        if (hasJavaSql) {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static <ARG, T> T callWhenHasJavaSql(Function<ARG, T> callable, ARG arg) {
        if (hasJavaSql) {
            return callable.apply(arg);
        }
        return null;
    }
}
