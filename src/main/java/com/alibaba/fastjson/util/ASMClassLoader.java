package com.alibaba.fastjson.util;

import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilterable;

public class ASMClassLoader extends ClassLoader {

    private static java.security.ProtectionDomain DOMAIN;
    
    private static Map<String, Class<?>> classMapping = new HashMap<String, Class<?>>();

    static {
        DOMAIN = (java.security.ProtectionDomain) java.security.AccessController.doPrivileged(new PrivilegedAction<Object>() {

            public Object run() {
                return ASMClassLoader.class.getProtectionDomain();
            }
        });
        
        classMapping.put(JavaBeanInfo.class.getName(), JavaBeanInfo.class);
        classMapping.put(SerializeFilterable.class.getName(), SerializeFilterable.class);
    }

    public ASMClassLoader(){
        super(getParentClassLoader());
    }

    public ASMClassLoader(ClassLoader parent){
        super (parent);
    }

    static ClassLoader getParentClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            try {
                contextClassLoader.loadClass(JSON.class.getName());
                return contextClassLoader;
            } catch (ClassNotFoundException e) {
                // skip
            }
        }
        return JSON.class.getClassLoader();
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> mappingClass = classMapping.get(name);
        if (mappingClass != null) {
            return mappingClass;
        }
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException e) {
            throw e;
        }
    }

    public Class<?> defineClassPublic(String name, byte[] b, int off, int len) throws ClassFormatError {
        Class<?> clazz = defineClass(name, b, off, len, DOMAIN);

        return clazz;
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
