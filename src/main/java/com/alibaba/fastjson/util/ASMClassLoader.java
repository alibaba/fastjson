package com.alibaba.fastjson.util;

import java.security.PrivilegedAction;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class ASMClassLoader extends ClassLoader {

    private static java.security.ProtectionDomain DOMAIN;

    static {
        DOMAIN = (java.security.ProtectionDomain) java.security.AccessController.doPrivileged(new PrivilegedAction<Object>() {

            public Object run() {
                return ASMClassLoader.class.getProtectionDomain();
            }
        });
    }

    public ASMClassLoader(){
        super(getParentClassLoader());
    }
    
    static ClassLoader getParentClassLoader() {
    	ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    	try {
			contextClassLoader.loadClass(JSON.class.getName());
			return contextClassLoader;
		} catch (ClassNotFoundException e) {
			// skip
		}
    	
    	return JSON.class.getClassLoader();
    }

    public Class<?> defineClassPublic(String name, byte[] b, int off, int len) throws ClassFormatError {
        Class<?> clazz = defineClass(name, b, off, len, DOMAIN);

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
    
    public boolean isExternalClass(Class<?> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();

        if (classLoader == null) {
            return false;
        }

        ClassLoader current = this;
        while (current != null) {
            if (current == classLoader) {
                return false;
            }
            
            current = current.getParent();
        }

        return true;
    }

}
