package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;

public class ASMClassLoader extends ClassLoader {

    public ASMClassLoader(){
        super(ASMClassLoader.class.getClassLoader());
    }

    public Class<?> defineClassPublic(String name, byte[] b, int off, int len) throws ClassFormatError {
        Class<?> clazz = defineClass(name, b, off, len, null);

        return clazz;
    }

    public static Class<?> forName(String className) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new JSONException("class nout found : " + className);
        }
    }
}
