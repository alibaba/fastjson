package com.alibaba.fastjson.util;

import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
import com.alibaba.fastjson.serializer.SerializeFilterable;
import com.alibaba.fastjson.serializer.SerializeWriter;

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
        classMapping.put(ObjectSerializer.class.getName(), ObjectSerializer.class);
        classMapping.put(JavaBeanSerializer.class.getName(), JavaBeanSerializer.class);
        classMapping.put(SerializeFilterable.class.getName(), SerializeFilterable.class);
        classMapping.put(SerializeBeanInfo.class.getName(), SerializeBeanInfo.class);
        classMapping.put(JSONSerializer.class.getName(), JSONSerializer.class);
        classMapping.put(JavaBeanDeserializer.class.getName(), JavaBeanDeserializer.class);
        classMapping.put(ParserConfig.class.getName(), ParserConfig.class);
        classMapping.put(SerializeWriter.class.getName(), SerializeWriter.class);
        classMapping.put(DefaultJSONParser.class.getName(), DefaultJSONParser.class);
        classMapping.put(JSONLexerBase.class.getName(), JSONLexerBase.class);
        classMapping.put(ParseContext.class.getName(), ParseContext.class);
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
